package ru.lingstra.communications.domain.result


sealed class ResultsPartialState(private val logMessage: String) {
    data class Error(val error: Throwable) : ResultsPartialState("Error $error")
    data class Loading(val loading: Boolean) : ResultsPartialState("Loading $loading")
    data class ResultsList(val results: List<FactResult>) : ResultsPartialState("Results $results")
    data class ResultPressed(val result: FactResult) : ResultsPartialState("Result pressed $result")

    override fun toString(): String = logMessage
    fun partial() = this
}