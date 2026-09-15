package com.jparkbro.community.impl.write

import android.net.Uri

/** 글 작성 화면의 첨부 이미지 한 장 - 기존에 올라가 있던 것과 이번에 새로 고른 것을 함께 다룬다. */
sealed interface CommunityWritePhoto {
    /** 목록/제거 매칭용 안정적인 키 - [Existing]은 imageId, [New]는 uri. */
    val key: Any

    /** 서버에 이미 업로드돼 있는 이미지 - 제거 안 하면 재업로드 없이 [imageId] 그대로 다시 보낸다.
     *  [bytes]/[url]은 미리보기용, 인증 조회 실패 등으로 [bytes]가 없으면 [url]로 대체한다. */
    data class Existing(
        val imageId: Long,
        val bytes: ByteArray?,
        val url: String?,
    ) : CommunityWritePhoto {
        override val key: Any = imageId
    }

    /** 이번에 새로 고른 로컬 이미지 - 등록/수정 시 업로드해서 새 imageId를 받는다. */
    data class New(val uri: Uri) : CommunityWritePhoto {
        override val key: Any = uri
    }
}
