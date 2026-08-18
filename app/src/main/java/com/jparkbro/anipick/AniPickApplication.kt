package com.jparkbro.anipick

import android.app.Application
import com.jparkbro.anipick.di.appModule
import com.jparkbro.auth.impl.di.authModule
import com.jparkbro.catalog.impl.di.catalogModule
import com.jparkbro.core.data.di.dataModule
import com.jparkbro.core.datastore.di.datastoreModule
import com.jparkbro.core.network.di.networkModule
import com.jparkbro.core.ui.di.uiModule
import com.jparkbro.explore.impl.di.exploreModule
import com.jparkbro.home.impl.di.homeModule
import com.jparkbro.mypage.impl.di.myPageModule
import com.jparkbro.ranking.impl.di.rankingModule
import com.jparkbro.search.impl.di.searchModule
import com.jparkbro.splash.impl.di.splashModule
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AniPickApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        KakaoSdk.init(this@AniPickApplication, BuildConfig.KAKAO_APP_KEY)

        startKoin {
            androidContext(this@AniPickApplication)

            modules(
                appModule,

                networkModule,
                datastoreModule,
                dataModule,
                uiModule,

                authModule,
                splashModule,
                homeModule,
                rankingModule,
                exploreModule,
                myPageModule,
                searchModule,
                catalogModule,
            )
        }
    }
}