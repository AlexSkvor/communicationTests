package ru.lingstra.communications.domain.app

import ru.lingstra.communications.system.NavigationManager

sealed class AppPartialState(private val logMessage: String) {

    object Refresh : AppPartialState("Refresh")
    data class Toast(val message: String) : AppPartialState("Toast $message")
    data class Progress(val value: Boolean) : AppPartialState("Progress $value")
    data class Navigation(val action: NavigationManager.NavigationAction) : AppPartialState("Navigation $action")

    override fun toString(): String = logMessage
}