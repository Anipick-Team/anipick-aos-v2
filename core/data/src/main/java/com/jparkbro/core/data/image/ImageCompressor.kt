package com.jparkbro.core.data.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.min
import kotlin.math.roundToInt

private const val MAX_DIMENSION = 1080
private const val MAX_UPLOAD_BYTES = 10L * 1024 * 1024
private const val MIN_QUALITY = 5
private const val START_QUALITY = 90

data class CompressedImage(
    val bytes: ByteArray,
    val mimeType: String,
    val extension: String,
)

/** 업로드 전 EXIF 회전 보정 + 리사이즈 + [maxBytes] 넘으면 품질 낮춰 재압축(그래도 넘으면 PNG는 JPEG로 전환해 재시도) */
fun ByteArray.compressImage(
    mimeType: String?,
    maxDimension: Int = MAX_DIMENSION,
    maxBytes: Long = MAX_UPLOAD_BYTES,
): CompressedImage {
    val original = BitmapFactory.decodeByteArray(this, 0, size)
        ?: return CompressedImage(this, mimeType ?: "image/jpeg", mimeType.toCompressFormat().extension())

    val rotated = original.rotate(readExifRotationDegrees())
    val resized = rotated.resizeIfLarger(maxDimension)

    val (output, format) = resized.compressUnderLimit(mimeType.toCompressFormat(), maxBytes)

    if (resized !== rotated) resized.recycle()
    if (rotated !== original) rotated.recycle()
    original.recycle()

    return CompressedImage(output, format.mimeType(), format.extension())
}

private fun ByteArray.readExifRotationDegrees(): Float {
    return try {
        val orientation = ExifInterface(ByteArrayInputStream(this))
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
    } catch (e: Exception) {
        0f
    }
}

private fun Bitmap.rotate(degrees: Float): Bitmap {
    if (degrees == 0f) return this
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

private fun Bitmap.resizeIfLarger(maxDimension: Int): Bitmap {
    val scale = min(1f, maxDimension.toFloat() / maxOf(width, height))
    if (scale >= 1f) return this
    return Bitmap.createScaledBitmap(this, (width * scale).toInt(), (height * scale).toInt(), true)
}

/** PNG는 quality 파라미터를 무시해서 재시도로 용량이 안 줄어든다 - 그래도 초과하면 JPEG로 전환해 다시 시도 */
private fun Bitmap.compressUnderLimit(
    format: Bitmap.CompressFormat,
    maxBytes: Long,
): Pair<ByteArray, Bitmap.CompressFormat> {
    var quality = START_QUALITY
    var output: ByteArray
    do {
        output = ByteArrayOutputStream().use { stream ->
            compress(format, quality, stream)
            stream.toByteArray()
        }
        quality -= (quality * 0.1).roundToInt()
    } while (output.size > maxBytes && quality > MIN_QUALITY && format != Bitmap.CompressFormat.PNG)

    if (format == Bitmap.CompressFormat.PNG && output.size > maxBytes) {
        return compressUnderLimit(Bitmap.CompressFormat.JPEG, maxBytes)
    }
    return output to format
}

private fun String?.toCompressFormat(): Bitmap.CompressFormat = when (this) {
    "image/png" -> Bitmap.CompressFormat.PNG
    "image/webp" -> Bitmap.CompressFormat.WEBP_LOSSLESS
    else -> Bitmap.CompressFormat.JPEG
}

private fun Bitmap.CompressFormat.mimeType(): String = when (this) {
    Bitmap.CompressFormat.PNG -> "image/png"
    Bitmap.CompressFormat.WEBP_LOSSLESS, Bitmap.CompressFormat.WEBP_LOSSY -> "image/webp"
    else -> "image/jpeg"
}

private fun Bitmap.CompressFormat.extension(): String = when (this) {
    Bitmap.CompressFormat.PNG -> "png"
    Bitmap.CompressFormat.WEBP_LOSSLESS, Bitmap.CompressFormat.WEBP_LOSSY -> "webp"
    else -> "jpg"
}
