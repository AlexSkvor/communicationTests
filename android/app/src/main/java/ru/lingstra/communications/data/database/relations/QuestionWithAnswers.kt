package ru.lingstra.communications.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.lingstra.communications.data.database.entities.AnswerEntity
import ru.lingstra.communications.data.database.entities.QuestionEntity

data class QuestionWithAnswers(
    @Embedded val question: QuestionEntity
) {
    @Relation(parentColumn = "id", entityColumn = "questionId")
    lateinit var answers: List<AnswerEntity>

    operator fun component2(): List<AnswerEntity> = answers
}