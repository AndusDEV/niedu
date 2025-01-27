package dev.andus.niedu

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import java.net.URL
import androidx.browser.customtabs.CustomTabsIntent

class UpdateChecker(private val context: Context) {

    data class ReleaseInfo(
        val version: String,
        val downloadUrl: String,
        val size: Long
    )

    private fun getCurrentVersion(): String {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    }

    private fun fetchLatestRelease(): ReleaseInfo? {
        return try {
            val url = URL("https://api.github.com/repos/AndusDEV/niedu/releases/latest")
            val connection = url.openConnection()
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")

            val response = connection.getInputStream().bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(response)

            val tagName = jsonObject.getString("tag_name").removePrefix("v")
            val assets = jsonObject.getJSONArray("assets")

            if (assets.length() > 0) {
                val downloadUrl = jsonObject.getString("html_url")
                val apkAsset = assets.getJSONObject(0)
                val size = apkAsset.getLong("size")

                ReleaseInfo(tagName, downloadUrl, size)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun compareVersions(current: String, latest: String): Boolean {
        val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }

        val maxLength = maxOf(currentParts.size, latestParts.size)
        val paddedCurrent = currentParts.padEnd(maxLength)
        val paddedLatest = latestParts.padEnd(maxLength)

        for (i in 0 until maxLength) {
            if (paddedLatest[i] > paddedCurrent[i]) return true
            if (paddedLatest[i] < paddedCurrent[i]) return false
        }
        return false
    }

    private fun List<Int>.padEnd(length: Int): List<Int> {
        return if (size >= length) this
        else this + List(length - size) { 0 }
    }

    private fun formatFileSize(bytes: Long): String {
        val mb = bytes / (1024.0 * 1024.0)
        return "%.1f MB".format(mb)
    }

    fun checkForUpdate() {
        Thread {
            val currentVersion = getCurrentVersion()
            val latestRelease = fetchLatestRelease()

            if (latestRelease != null && compareVersions(currentVersion, latestRelease.version)) {
                (context as MainActivity).runOnUiThread {
                    showUpdateDialog(currentVersion, latestRelease)
                }
            }
        }.start()
    }

    private fun showUpdateDialog(currentVersion: String, releaseInfo: ReleaseInfo) {
        val message = """
            Nowa aktualizacja dostępna!
            
            Aktualna wersja: v$currentVersion
            Najnowsza wersja: v${releaseInfo.version}
            Rozmiar pliku do pobrania: ${formatFileSize(releaseInfo.size)}
            
            Czy chciałbyś pobrać plik .apk?
        """.trimIndent()

        AlertDialog.Builder(context)
            .setTitle("Aktualizacja dostępna")
            .setMessage(message)
            .setPositiveButton("Pobierz") { _, _ ->
                val intent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .setUrlBarHidingEnabled(true)
                    .build()
                intent.launchUrl(context, Uri.parse(releaseInfo.downloadUrl))
            }
            .setNegativeButton("Później", null)
            .show()
    }
}