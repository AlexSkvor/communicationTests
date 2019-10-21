package ru.lingstra.communications.domain.test_list

import ru.lingstra.communications.domain.models.Test

sealed class TestsListPartialState(private val logMessage: String) {

    data class Error(val error: Throwable) : TestsListPartialState("Error $error")
    data class Loading(val loading: Boolean) : TestsListPartialState("Loading $loading")
    data class TestsList(val tests: List<Test>) : TestsListPartialState("Tests $tests")
    data class TestPressed(val test: Test) : TestsListPartialState("Test: $test")

    override fun toString(): String = logMessage
    fun partial() = this
}