package dev.andus.niedu

import org.mozilla.geckoview.GeckoSession

class MyNavigationDelegate : GeckoSession.NavigationDelegate {
    var canGoBack: Boolean = false

    override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
        this.canGoBack = canGoBack
    }
}

val navigationDelegate = MyNavigationDelegate()