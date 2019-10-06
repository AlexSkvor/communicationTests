package ru.lingstra.communications.domain.test_list

sealed class TestsListPartialState(private val logMessage: String) {

    data class Error(val error: Throwable) : TestsListPartialState("Error $error")
    data class Loaading(val loading: Boolean): TestsListPartialState("Loading $loading")

    override fun toString(): String = logMessage
    fun partial() = this
}