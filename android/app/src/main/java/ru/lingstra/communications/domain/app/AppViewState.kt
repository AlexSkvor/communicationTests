package ru.lingstra.communications.domain.app

import ru.lingstra.communications.system.NavigationManager

data class AppViewState(
    val toast: String = "",
    val snackBar: String = "",
    val progress: Boolean = false,
    val render: Render = Render.NONE,
    val navigationAction: NavigationManager.NavigationAction = NavigationManager.NavigationAction.Back
) {
    enum class Render {
        TOAST, PROGRESS, NAVIGATION, NONE
    }
}