package com.jefftrotz.fetchcodingexercise

import android.app.Application
import com.jefftrotz.fetchcodingexercise.modules.activityModule
import com.jefftrotz.fetchcodingexercise.modules.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule, activityModule)
        }
    }
}