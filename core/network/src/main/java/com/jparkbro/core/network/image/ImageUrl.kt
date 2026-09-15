package com.jparkbro.core.network.image

import com.jparkbro.core.network.BuildConfig

/** 이미지 id를 서버 이미지 조회 URL로 변환 */
fun Long.toImageUrl(): String = "${BuildConfig.BASE_URL}/image/$this"

/** 서버 이미지 URL(`.../image/{id}`) 끝의 id만 추출 */
fun String.toImageId(): Long? = substringAfterLast('/').toLongOrNull()
