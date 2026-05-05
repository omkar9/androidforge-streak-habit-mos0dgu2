package com.androidforge.streakhappit

import android.app.Application
import com.androidforge.streakhappit.core.ads.AdMobManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class StreakHabitApplication : Application() {

    @Inject
    lateinit var adMobManager: AdMobManager

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize AdMob SDK early
        adMobManager.initialize()
    }
}