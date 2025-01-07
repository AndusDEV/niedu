package dev.andus.niedu

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSession.NavigationDelegate

class MyNavigationDelegate(private val context: Context) : NavigationDelegate {
    var canGoBack: Boolean = false

    override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
        this.canGoBack = canGoBack
    }

    override fun onLoadRequest(session: GeckoSession, request: NavigationDelegate.LoadRequest): GeckoResult<AllowOrDeny> {
        if (request.target == NavigationDelegate.TARGET_WINDOW_NEW) {
            var uri = request.uri

            val intent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .setUrlBarHidingEnabled(true)
                .build()
            intent.launchUrl(context, Uri.parse(uri))
        }

        return GeckoResult.fromValue(AllowOrDeny.ALLOW)
    }
}