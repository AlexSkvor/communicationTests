package ru.lingstra.communications.domain.test_passing

import ru.lingstra.communications.domain.models.Test

sealed class TestPassingPartialState(private val logMessage: String) {

    data class Answer(val answer: Pair<String, Test.Question>) :
        TestPassingPartialState("Answer $answer")

    data class ShowResult(val result: Test.Result) : TestPassingPartialState("ShowResult $result")
    data class Loading(val loading: Boolean) : TestPassingPartialState("Loading $loading")
    data class Error(val t: Throwable) : TestPassingPartialState("Error $t")

    object Start: TestPassingPartialState("Start")

    fun partial() = this
    override fun toString(): String = logMessage
}