package ru.lingstra.avitocopy.domain.app

data class AppViewState(
    val toast: String = "",
    val snackBar: String = "",
    val progress: Boolean = false,
    val render: Render = Render.NONE
) {
    enum class Render {
        TOAST, PROGRESS, NONE
    }
}