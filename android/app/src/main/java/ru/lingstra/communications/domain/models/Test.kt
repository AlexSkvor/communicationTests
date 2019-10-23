package ru.lingstra.communications.domain.models

import ru.lingstra.communications.data.database.entities.ResultReferenceEntity
import ru.lingstra.communications.data.database.entities.TestEntity
import ru.lingstra.communications.data.database.relations.toData
import java.util.*

data class Test(
    val id: String,
    val name: String,
    val description: String,
    val questions: List<Question>,
    val results: List<Result>
) {
    data class Question(
        val id: String = "",
        val text: String,
        val answers: List<Answer>
    )

    data class Answer(
        val id: String = "",
        val text: String,
        val mark: Int
    )

    data class Result(
        val id: String = "",
        val minMark: Int,
        val maxMark: Int,
        val text: String
    )

    private val uuid: String
        get() = UUID.randomUUID().toString()

    fun toEntity(): ru.lingstra.communications.data.database.relations.Test {
        val r: List<Result> = this.results
        val q: List<Question> = this.questions
        return ru.lingstra.communications.data.database.relations.Test(
            innerTest = TestEntity(
                id,
                name,
                description
            )
        ).apply {
            results = r.map {
                ResultReferenceEntity(
                    id = if (it.id.isEmpty()) uuid else it.id,
                    testId = innerTest.id,
                    minMark = it.minMark,
                    maxMark = it.maxMark,
                    resultDescription = it.text
                )
            }
            questions = q.toData(innerTest.id)
        }
    }
}