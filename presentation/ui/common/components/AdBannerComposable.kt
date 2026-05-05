package com.androidforge.streakhappit.presentation.ui.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.androidforge.streakhappit.core.ads.AdConstants
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import timber.log.Timber

@Composable
fun AdBannerComposable(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {\ context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = AdConstants.BANNER_AD_UNIT_ID
                loadAd(AdRequest.Builder().build())
                adListener = object : AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Timber.e("AdMob Banner failed to load: %s", adError.message)
                    }

                    override fun onAdLoaded() {
                        Timber.d("AdMob Banner loaded successfully.")
                    }
                }
            }
        }
    )
}