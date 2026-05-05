package com.androidforge.streakhappit.core.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobManager @Inject constructor(
    private val applicationContext: Context
) {

    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialAdLoading = false

    fun initialize() {
        MobileAds.initialize(applicationContext) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterStatus in statusMap.values) {
                Timber.d("AdMob adapter: %s, Description: %s, Latency: %s", adapterStatus.adapterClassName, adapterStatus.description, adapterStatus.state)
            }
            Timber.d("AdMob SDK initialized.")
            loadInterstitialAd()
        }
    }

    fun loadInterstitialAd() {
        if (interstitialAd == null && !isInterstitialAdLoading) {
            isInterstitialAdLoading = true
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                applicationContext,
                AdConstants.INTERSTITIAL_AD_UNIT_ID,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Timber.e("AdMob Interstitial failed to load: %s", adError.message)
                        interstitialAd = null
                        isInterstitialAdLoading = false
                    }

                    override fun onAdLoaded(ad: InterstitialAd) {
                        Timber.d("AdMob Interstitial loaded successfully.")
                        interstitialAd = ad
                        isInterstitialAdLoading = false
                    }
                }
            )
        } else if (interstitialAd != null) {
            Timber.d("Interstitial ad already loaded.")
        } else if (isInterstitialAdLoading) {
            Timber.d("Interstitial ad is already loading.")
        }
    }

    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit = {}) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Timber.d("AdMob Interstitial was dismissed.")
                    interstitialAd = null
                    loadInterstitialAd() // Pre-load the next ad
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Timber.e("AdMob Interstitial failed to show: %s", adError.message)
                    interstitialAd = null
                    loadInterstitialAd() // Try to load a new ad
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {
                    Timber.d("AdMob Interstitial showed.")
                }
            }
            interstitialAd?.show(activity)
        } else {
            Timber.w("AdMob Interstitial not ready. Loading new one.")
            loadInterstitialAd() // Try to load a new ad if not ready
            onAdDismissed()
        }
    }
}