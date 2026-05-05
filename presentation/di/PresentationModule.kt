package com.androidforge.streakhappit.presentation.di

import android.content.Context
import com.androidforge.streakhappit.core.ads.AdMobManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    @Singleton
    fun provideAdMobManager(@ApplicationContext context: Context): AdMobManager {
        return AdMobManager(context)
    }

    // Add other presentation-specific dependencies here if needed
}