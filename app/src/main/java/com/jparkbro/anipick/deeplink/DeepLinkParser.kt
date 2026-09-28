package com.jparkbro.anipick.deeplink

import android.net.Uri
import androidx.navigation3.runtime.NavKey
import com.jparkbro.catalog.api.CatalogNavKey
import com.jparkbro.community.api.CommunityNavKey

/**
 * 딥링크 Uri의 path를 목적지 NavKey로 변환한다. 매칭되는 경로가 없으면 null.
 * 경로 계약(shareLink 생성 쪽과 반드시 맞춰야 함):
 *  - /app/anime/detail/{animeId}   -> CatalogNavKey.Anime (v1부터 쓰던 형식 - 이미 뿌려진 링크 호환 위해 유지)
 *  - /app/community/post/{postId}  -> CommunityNavKey.Detail (v2 신규)
 */
fun parseDeepLink(uri: Uri): NavKey? {
    val segments = uri.pathSegments
    if (segments.getOrNull(0) != "app") return null

    return when (segments.getOrNull(1)) {
        "anime" -> {
            if (segments.getOrNull(2) != "detail") return null
            segments.getOrNull(3)?.toLongOrNull()?.let { CatalogNavKey.Anime(it) }
        }
        "community" -> {
            if (segments.getOrNull(2) != "post") return null
            segments.getOrNull(3)?.toLongOrNull()?.let { CommunityNavKey.Detail(it) }
        }
        else -> null
    }
}
