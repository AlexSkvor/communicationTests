package ru.lingstra.communications.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.lingstra.communications.data.database.entities.AnswerEntity
import ru.lingstra.communications.data.database.entities.QuestionEntity
import ru.lingstra.communications.domain.models.Test
import java.util.*

data class QuestionWithAnswers(
    @Embedded val question: QuestionEntity
) {

    @Relation(parentColumn = "id", entityColumn = "questionId")
    lateinit var answers: List<AnswerEntity>

    operator fun component2(): List<AnswerEntity> = answers

    fun toDomain(): Test.Question =
        Test.Question(text = question.text,
            answers = answers.map { Test.Answer(it.id, it.text, it.mark) })
}

fun List<QuestionWithAnswers>.toDomain(): List<Test.Question> = map { it.toDomain() }

fun List<Test.Question>.toData(testId: String): List<QuestionWithAnswers> =
    map {
        QuestionWithAnswers(
            question = QuestionEntity(
                id = if (it.id.isEmpty()) uuid else it.id,
                testId = testId,
                text = it.text
            )
        ).apply {
            answers = it.answers.map { dom ->
                AnswerEntity(
                    id = if (dom.id.isEmpty()) uuid else dom.id,
                    questionId = this.question.id,
                    text = dom.text,
                    mark = dom.mark
                )
            }
        }
    }

private val uuid: String
    get() = UUID.randomUUID().toString()