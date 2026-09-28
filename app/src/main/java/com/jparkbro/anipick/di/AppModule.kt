package com.jparkbro.anipick.di

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.jparkbro.anipick.deeplink.DeepLinkViewModel
import com.jparkbro.anipick.update.UpdateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::DeepLinkViewModel)

    single<AppUpdateManager> { AppUpdateManagerFactory.create(androidContext()) }
    viewModelOf(::UpdateViewModel)
}