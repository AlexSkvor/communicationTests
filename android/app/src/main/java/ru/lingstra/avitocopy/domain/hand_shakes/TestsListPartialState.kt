package ru.lingstra.avitocopy.domain.hand_shakes

sealed class TestsListPartialState(private val logMessage: String) {

    data class Error(val error: Throwable) : TestsListPartialState("Error $error")

    override fun toString(): String = logMessage
    fun partial() = this
}