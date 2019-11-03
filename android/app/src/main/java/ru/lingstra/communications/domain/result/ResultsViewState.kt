package ru.lingstra.communications.domain.result

data class ResultsViewState(
    val results: List<FactResult> = emptyList()
)