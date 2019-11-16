package ru.lingstra.communications.domain.test_passing

import ru.lingstra.communications.domain.models.Test

data class TestPassingViewState(
    val test: Test,
    val answers: Map<String, Test.Question> = mapOf(),
    val result: Test.Result? = null,
    val started: Boolean = false
)