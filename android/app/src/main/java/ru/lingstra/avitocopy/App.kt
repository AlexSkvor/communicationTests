package ru.lingstra.avitocopy

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.lingstra.avitocopy.toothpick.AppModule
import ru.lingstra.avitocopy.toothpick.DI
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initToothpick()
        openScope()
        initThreetenABP()
        BuildConfig.APPLICATION_ID
    }

    private fun initThreetenABP() {
        AndroidThreeTen.init(this)
    }

    private fun openScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this, "https://api.vk.com/method/"))
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().preventMultipleRootScopes())
        }
    }

}