package dev.andus.niedu.models

data class Patch(
    val name: String,
    val description: String,
    val files: Files,
    val allowedHostsCss: List<String> = emptyList()
)

data class Files(
    val js: List<String> = emptyList(),
    val css: List<String> = emptyList()
)
