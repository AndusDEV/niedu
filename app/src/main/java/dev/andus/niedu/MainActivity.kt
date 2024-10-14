package dev.andus.niedu

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var sharedPreferences: SharedPreferences
    private var baseUrl: String? = null
    private var city: String? = null
    private var journalType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        journalType = getJournalType()
        if (journalType == null) {
            showJournalTypeDialog()
        }

        baseUrl = getBaseUrl()
        webView = WebView(this)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        frameLayout.addView(webView)
        setupWebView()

        val fab = findViewById<FloatingActionButton>(R.id.changeAccountFab)
        fab.setOnClickListener {
            showJournalTypeDialog()
        }

        loadLoginPage()
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
            loadLoginPage()
        } else {
            val fullUrl = "$baseUrl$endpoint"
            if (webView.url != fullUrl) {
                webView.loadUrl(fullUrl)
            }
        }
    }

    private fun loadLoginPage() {
        if (journalType == "zwykły" && city == null) {
            showCityInputDialog()
        } else {
            val loginUrl = when (journalType) {
                "zwykły" -> "https://dziennik-uczen.vulcan.net.pl/$city/LoginEndpoint.aspx"
                else -> "https://eduvulcan.pl/logowanie"
            }
            webView.loadUrl(loginUrl)
        }
    }

    private fun saveBaseUrl(url: String) {
        val currentBaseUrl = getBaseUrl()
        val newBaseUrl = url.substringBeforeLast("/")

        if (currentBaseUrl == null || currentBaseUrl != newBaseUrl) {
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
        city = url.substringAfter("${getDomain()}/").substringBefore("/App")
        println("Wyciągnięto miasto: $city")
    }

    private fun getBaseUrl(): String? {
        return sharedPreferences.getString("base_url", null)
    }

    private fun getJournalType(): String? {
        return sharedPreferences.getString("journal_type", null)
    }

    private fun getDomain(): String {
        return when (journalType) {
            "zwykły" -> "dziennik-uczen.vulcan.net.pl"
            else -> "uczen.eduvulcan.pl"
        }
    }

    private fun saveJournalType(type: String) {
        with(sharedPreferences.edit()) {
            putString("journal_type", type)
            apply()
        }
        journalType = type
    }

    private fun showJournalTypeDialog() {
        val options = arrayOf("edu", "zwykły")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Wybierz typ dziennika")
        builder.setItems(options) { _, which ->
            val selectedType = options[which]
            saveJournalType(selectedType)
            Toast.makeText(this, "Wybrano: $selectedType", Toast.LENGTH_SHORT).show()
            if (selectedType == "zwykły") {
                showCityInputDialog()
            } else {
                loadLoginPage()
            }
        }
        builder.show()
    }

    private fun showCityInputDialog() {
        val input = EditText(this)
        input.hint = "Wpisz miasto"
        val dialog = AlertDialog.Builder(this)
            .setTitle("Wpisz miasto")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val cityInput = input.text.toString()
                if (cityInput.isNotEmpty()) {
                    city = cityInput
                    loadLoginPage()
                } else {
                    Toast.makeText(this, "Miasto nie może być puste", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Anuluj") { dialog, _ -> dialog.cancel() }
            .create()

        dialog.show()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
