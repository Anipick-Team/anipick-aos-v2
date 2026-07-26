package com.jparkbro.anipick

import android.app.Application
import com.jparkbro.anipick.di.appModule
import com.jparkbro.auth.impl.di.authModule
import com.jparkbro.core.data.di.dataModule
import com.jparkbro.splash.impl.di.splashModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AniPickApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AniPickApplication)

            modules(
                appModule,

                dataModule,

                authModule,
                splashModule,
            )
        }
    }
}