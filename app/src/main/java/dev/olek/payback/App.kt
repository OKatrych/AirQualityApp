package dev.olek.payback

import android.app.Application
import dev.olek.data.di.DataModule
import dev.olek.payback.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(DataModule().module, AppModule().module)
        }
    }
}