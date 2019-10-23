package ru.lingstra.communications.domain.test_passing

import ru.lingstra.communications.domain.models.Test

sealed class TestPassingPartialState(private val logMessage: String) {

    data class Answer(val answer: Pair<String, Test.Question>) :
        TestPassingPartialState("Answer $answer")

    object ShowResult : TestPassingPartialState("ShowResult")

    fun partial() = this
    override fun toString(): String = logMessage
}