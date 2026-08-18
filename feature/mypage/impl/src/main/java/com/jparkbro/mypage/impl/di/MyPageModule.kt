package com.jparkbro.mypage.impl.di

import com.jparkbro.mypage.impl.detail.MyPageDetailViewModel
import com.jparkbro.mypage.impl.main.MyPageMainViewModel
import com.jparkbro.mypage.impl.setting.detail.SettingDetailViewModel
import com.jparkbro.mypage.impl.setting.main.SettingMainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myPageModule = module {
    viewModelOf(::MyPageMainViewModel)
    viewModelOf(::MyPageDetailViewModel)
    viewModelOf(::SettingMainViewModel)
    viewModelOf(::SettingDetailViewModel)
}
