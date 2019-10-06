package ru.lingstra.communications.domain.models

data class Test(
    val id: String,
    val name: String,
    val description: String,
    val questions: List<Question>,
    val results: List<Result>
) {
    data class Question(
        val text: String,
        val answers: List<Answer>
    )

    data class Answer(
        val text: String,
        val mark: Int
    )

    data class Result(
        val minMark: Int,
        val maxMark: Int,
        val text: String
    )
}