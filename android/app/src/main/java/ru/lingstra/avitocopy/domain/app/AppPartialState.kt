package ru.lingstra.avitocopy.domain.app

sealed class AppPartialState(private val logMessage: String) {

    object Refresh : AppPartialState("Refresh")
    data class Toast(val message: String) : AppPartialState("Toast $message")
    data class Snackbar(val message: String) : AppPartialState("Snackbar $message")
    data class Progress(val value: Boolean) : AppPartialState("Progress $value")

    override fun toString(): String = logMessage
}