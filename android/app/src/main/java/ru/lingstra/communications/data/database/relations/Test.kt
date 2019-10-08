package ru.lingstra.communications.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.lingstra.communications.data.database.entities.AnswerEntity
import ru.lingstra.communications.data.database.entities.ResultReferenceEntity
import ru.lingstra.communications.data.database.entities.TestEntity
import ru.lingstra.communications.domain.models.Test
import java.util.*

data class Test(
    @Embedded val innerTest: TestEntity
) {

    private val uuid: String
        get() = UUID.randomUUID().toString()

    @Relation(parentColumn = "id", entityColumn = "questionId", entity = AnswerEntity::class)
    lateinit var questions: List<QuestionWithAnswers>

    operator fun component2(): List<QuestionWithAnswers> = questions

    @Relation(parentColumn = "id", entityColumn = "testId", entity = ResultReferenceEntity::class)
    lateinit var results: List<ResultReferenceEntity>

    operator fun component3(): List<ResultReferenceEntity> = results

    fun toDomain(): Test = Test(
        id = innerTest.id,
        name = innerTest.name,
        description = innerTest.description,
        questions = questions.toDomain(),
        results = results.map {
            Test.Result(
                minMark = it.minMark,
                maxMark = it.maxMark,
                text = it.resultDescription
            )
        }
    )
}