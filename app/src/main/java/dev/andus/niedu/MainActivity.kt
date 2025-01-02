package dev.andus.niedu

import android.content.SharedPreferences
import android.os.Bundle
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.mozilla.geckoview.GeckoSessionSettings

class MainActivity : AppCompatActivity() {

    private lateinit var geckoView: GeckoView
    private lateinit var geckoSession: GeckoSession
    private lateinit var sharedPreferences: SharedPreferences
    private var baseUrl: String? = null
    private var city: String? = null
    private var journalType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)

        journalType = getJournalType()
        if (journalType == null) {
            showJournalTypeDialog()
        }

        city = getCity()
        baseUrl = getBaseUrl()

        geckoView = findViewById(R.id.geckoView)
        setupGeckoView()

        val fab = findViewById<FloatingActionButton>(R.id.changeAccountFab)
        fab.setOnClickListener {
            showJournalTypeDialog()
        }

        loadLoginPage()
    }

    private fun setupGeckoView() {
        val runtime = GeckoRuntime.create(this)
        val settings = GeckoSessionSettings.Builder()
            .allowJavascript(true)
            .userAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE)
            .build()

        geckoSession = GeckoSession(settings)
        geckoSession.navigationDelegate = navigationDelegate
        geckoSession.open(runtime)
        geckoView.setSession(geckoSession)
    }

    private fun loadLoginPage() {
        if (journalType == "zwykły" && city == null) {
            showCityInputDialog()
        } else {
            val loginUrl = when (journalType) {
                "zwykły" -> "https://dziennik-uczen.vulcan.net.pl/$city/LoginEndpoint.aspx"
                else -> "https://eduvulcan.pl/logowanie"
            }
            geckoSession.loadUri(loginUrl)
        }
    }

    private fun saveCity(city: String) {
        with(sharedPreferences.edit()) {
            putString("city", city)
            apply()
        }
    }

    private fun getCity(): String? {
        return sharedPreferences.getString("city", null)
    }

    private fun getBaseUrl(): String? {
        return sharedPreferences.getString("base_url", null)
    }

    private fun getJournalType(): String? {
        return sharedPreferences.getString("journal_type", null)
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
            loadLoginPage()
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
                    saveCity(cityInput)
                    loadLoginPage()
                } else {
                    Toast.makeText(this, "Miasto nie może być puste", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Anuluj") { dialog, _ -> dialog.cancel() }
            .create()

        dialog.show()
    }

    @Deprecated("")
    override fun onBackPressed() {
        if (navigationDelegate.canGoBack) {
            geckoSession.goBack()
        } else {
            super.onBackPressed()
        }
    }
    override fun onDestroy() {
        geckoSession.close()
        super.onDestroy()
    }
}