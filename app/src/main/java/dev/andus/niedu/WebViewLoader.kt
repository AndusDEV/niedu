package dev.andus.niedu

import android.content.Context
import android.util.Log
import android.webkit.WebView
import dev.andus.niedu.models.Patch
import org.json.JSONArray
import java.io.FileNotFoundException
import java.lang.Exception

class WebViewLoader(private val context: Context, private val webView: WebView) {
    // Function to load and apply patches
    fun loadAndApplyPatches(currentUrl: String) {
        try {
            val patches = loadPatchesFromJson()
            val scriptsToLoad = mutableListOf<String>()
            val stylesToLoad = mutableListOf<String>()

            // Load patches and scripts as per your existing logic
            for (patch in patches) {
                // Load CSS files if allowed
                if (patch.allowedHostsCss.isNotEmpty() && patch.allowedHostsCss.any { currentUrl.contains(it) }) {
                    for (cssFile in patch.files.css) {
                        val css = loadFileFromAssets("ifv/patches/$cssFile")
                        if (css.isNotEmpty()) {
                            stylesToLoad.add(css)
                        }
                    }
                }

                // Load JavaScript files
                for (jsFile in patch.files.js) {
                    val script = loadFileFromAssets("ifv/patches/$jsFile")
                    if (script.isNotEmpty()) {
                        scriptsToLoad.add(script)
                    }
                }
            }

            // Inject CSS and JavaScript into the WebView
            injectCss(stylesToLoad)
            injectJavaScript(scriptsToLoad)
        } catch (e: Exception) {
            Log.e("WebViewLoader", "Error loading patches: ${e.message}", e)
        }
    }

    private fun loadPatchesFromJson(): List<Patch> {
        return try {
            val json = context.assets.open("ifv/patches.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)
            val patches = mutableListOf<Patch>()

            for (i in 0 until jsonArray.length()) {
                val patchJson = jsonArray.getJSONObject(i)
                val filesJson = patchJson.getJSONObject("files")

                val jsFiles = filesJson.optJSONArray("js")?.let { (0 until it.length()).map { idx -> it.getString(idx) } } ?: emptyList()
                val cssFiles = filesJson.optJSONArray("css")?.let { (0 until it.length()).map { idx -> it.getString(idx) } } ?: emptyList()
                val allowedHostsCss = patchJson.optJSONArray("allowedHostsCss")?.let { (0 until it.length()).map { idx -> it.getString(idx) } } ?: emptyList()

                val patch = Patch(
                    name = patchJson.getString("name"),
                    description = patchJson.getString("description"),
                    files = dev.andus.niedu.models.Files(js = jsFiles, css = cssFiles),
                    allowedHostsCss = allowedHostsCss
                )
                patches.add(patch)
            }
            patches
        } catch (e: Exception) {
            Log.e("WebViewLoader", "Error loading patches from JSON: ${e.message}", e)
            emptyList() // Return an empty list on error
        }
    }

    private fun loadFileFromAssets(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            Log.e("WebViewLoader", "File not found: $fileName", e)
            ""
        } catch (e: Exception) {
            Log.e("WebViewLoader", "Error reading file: $fileName", e)
            ""
        }
    }

    private fun injectCss(styles: List<String>) {
        styles.forEach { css ->
            val cssScript = """
                var style = document.createElement('style');
                style.innerHTML = `${css.replace("`", "\\`")}`; // Use template literals for safety
                document.head.appendChild(style);
            """.trimIndent()
            webView.evaluateJavascript(cssScript, null)
        }
    }

    private fun injectJavaScript(scripts: List<String>) {
        scripts.forEach { script ->
            val moduleScript = """
        (function() {
            console.log('Injecting script');
            const scriptContent = `${script.replace("`", "\\`").replace("$", "\\$")}`;
            console.log('Script content:', scriptContent);
            const script = document.createElement('script');
            script.type = 'module';
            script.textContent = scriptContent;
            document.head.appendChild(script);
        })();
        """.trimIndent()
            webView.evaluateJavascript(moduleScript, null)
        }
    }
}
