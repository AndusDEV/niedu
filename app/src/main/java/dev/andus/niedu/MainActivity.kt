package dev.andus.niedu

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var sharedPreferences: SharedPreferences
    private var baseUrl: String? = null
    private var city: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        baseUrl = getBaseUrl()
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        webView = WebView(this)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        frameLayout.addView(webView)
        setupWebView()

        tabLayout.addTab(tabLayout.newTab().setText("Strona główna"))
        tabLayout.addTab(tabLayout.newTab().setText("Wiadomości"))
        tabLayout.addTab(tabLayout.newTab().setText("Frekwencja"))
        tabLayout.addTab(tabLayout.newTab().setText("Oceny"))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadUrl("/tablica") // Strona główna
                    1 -> loadMessagesUrl() // Wiadomości
                    2 -> loadUrl("/frekwencja") // Frekwencja
                    3 -> loadUrl("/oceny") // Oceny
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        webView.loadUrl("https://eduvulcan.pl/logowanie")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null && url.contains("/App/")) {
                    saveBaseUrl(url)
                    extractCity(url)
                }
            }
        }

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.builtInZoomControls = true
        webSettings.setSupportZoom(true)
        webSettings.mediaPlaybackRequiresUserGesture = false

        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
    }

    private fun loadUrl(endpoint: String) {
        if (baseUrl == null) {
            webView.loadUrl("https://eduvulcan.pl/logowanie")
        } else {
            val fullUrl = "$baseUrl$endpoint"
            if (webView.url != fullUrl) {
                webView.loadUrl(fullUrl)
            }
        }
    }

    private fun loadMessagesUrl() {
        if (city != null) {
            val messagesUrl = "https://wiadomosci.eduvulcan.pl/$city/App/odebrane"
            webView.loadUrl(messagesUrl)
        } else {
            webView.loadUrl("https://eduvulcan.pl/logowanie")
        }
    }

    private fun saveBaseUrl(url: String) {
        val currentBaseUrl = getBaseUrl()
        if (currentBaseUrl == null) {
            val newBaseUrl = url.substringBeforeLast("/")
            with(sharedPreferences.edit()) {
                putString("base_url", newBaseUrl)
                apply()
                baseUrl = newBaseUrl
                println("Zapisano baseUrl: $newBaseUrl")
            }
        } else {
            println("BaseUrl już ustawione: $currentBaseUrl")
        }
    }

    private fun extractCity(url: String) {
        city = url.substringAfter("uczen.eduvulcan.pl/").substringBefore("/App")
        println("Wyciągnięto miasto: $city")
    }

    private fun getBaseUrl(): String? {
        return sharedPreferences.getString("base_url", null)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
