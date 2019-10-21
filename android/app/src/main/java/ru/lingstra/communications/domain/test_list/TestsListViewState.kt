package ru.lingstra.communications.domain.test_list

import ru.lingstra.communications.domain.models.Test

data class TestsListViewState(
    val tests: List<Test> = listOf()
)