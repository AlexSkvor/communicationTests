package ru.lingstra.communications.data.database.dao

import androidx.room.*
import io.reactivex.Single
import ru.lingstra.communications.data.database.entities.AnswerEntity
import ru.lingstra.communications.data.database.entities.QuestionEntity
import ru.lingstra.communications.data.database.relations.QuestionWithAnswers

@Dao
abstract class QuestionDao : BaseDao<QuestionEntity> {

    @Query("SELECT * from questions WHERE id = :key")
    abstract fun getQuestionWithAnswersById(key: String): Single<QuestionWithAnswers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAnswers(attributes: List<AnswerEntity>)

    @Transaction
    open fun insertQuestionWithAnswers(questionWithAnswers: QuestionWithAnswers) {
        insert(questionWithAnswers.question)
        insertAnswers(questionWithAnswers.answers)
    }

    @Transaction
    open fun insertQuestionsWithAnswers(questionsWithAnswers: List<QuestionWithAnswers>) {
        val (objectList, attributeList) =
            questionsWithAnswers.fold(
                Pair<MutableList<QuestionEntity>, MutableList<AnswerEntity>>(
                    arrayListOf(),
                    arrayListOf()
                ), { (questionsList, answerList), (question, answers) ->
                    questionsList.add(question)
                    answerList.addAll(answers)
                    questionsList to answerList
                })
        insertAll(objectList)
        insertAnswers(attributeList)
    }
}