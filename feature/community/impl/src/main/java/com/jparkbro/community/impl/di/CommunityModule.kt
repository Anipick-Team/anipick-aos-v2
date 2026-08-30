package com.jparkbro.community.impl.di

import com.jparkbro.community.impl.detail.CommunityDetailViewModel
import com.jparkbro.community.impl.main.CommunityMainViewModel
import com.jparkbro.community.impl.write.CommunityWriteViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/** nullable 파라미터가 섞인 ViewModel은 `params.get()`(타입 기준 조회) 대신 `params[i]`(인덱스)로 꺼낸다 -
 *  넘긴 값이 null이면 타입 기준 조회가 그 자리를 건너뛰고 "같은 타입의 첫 번째 값"으로 대체해버려서,
 *  예를 들어 `parametersOf(seriesId, null)`에서 postId가 seriesId로 채워진다(= 글쓰기가 수정 모드로 열린다). */
val communityModule = module {
    viewModel { params ->
        CommunityMainViewModel(
            seriesId = params[0],
            title = params[1],
            coverImageUrl = params[2],
            genres = params[3],
            communityRepository = get(),
            globalSnackbarManager = get(),
        )
    }
    viewModel { params ->
        CommunityDetailViewModel(
            postId = params[0],
            communityRepository = get(),
            globalSnackbarManager = get(),
        )
    }
    viewModel { params ->
        CommunityWriteViewModel(
            seriesId = params[0],
            postId = params[1],
            communityRepository = get(),
            globalSnackbarManager = get(),
            context = get(),
        )
    }
}
