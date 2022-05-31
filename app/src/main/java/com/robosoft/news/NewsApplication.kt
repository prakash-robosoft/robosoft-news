package com.robosoft.news

import android.app.Application
import com.robosoft.news.di.appModule
import com.robosoft.news.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NewsApplication)
            modules(
                listOf(
                    networkModule,
                    appModule
                )
            )
        }
    }
}
