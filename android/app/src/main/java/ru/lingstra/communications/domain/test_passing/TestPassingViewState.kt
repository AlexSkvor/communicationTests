package ru.lingstra.communications.domain.test_passing

import ru.lingstra.communications.domain.models.Test

data class TestPassingViewState(
    val test: Test,
    val answers: Map<Test.Question, Test.Answer> = mapOf()
) {
}