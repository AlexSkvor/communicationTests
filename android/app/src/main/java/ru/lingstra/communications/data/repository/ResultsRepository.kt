package ru.lingstra.communications.data.repository

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.joda.time.DateTime
import ru.lingstra.communications.data.database.dao.*
import ru.lingstra.communications.data.database.entities.AnswerEntity
import ru.lingstra.communications.data.database.entities.TestPassingEntity
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.result.FactResult
import ru.lingstra.communications.flattenMap
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import javax.inject.Inject

class ResultsRepository @Inject constructor(
    private val testPassingDao: TestPassingDao,
    private val testDao: TestDao,
    private val schedulers: SchedulersProvider,
    private val factAnswerDao: FactAnswerDao,
    private val answerDao: AnswerDao
) {

    fun getResults(user: UserEntity): Observable<List<FactResult>> =
        testPassingDao.getTestPassingsForUser(user.id)
            .flattenAsFlowable { it }
            .flatMapSingle { convertTestPassingToResult(it, user) }
            .toList()
            .toObservable()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun convertTestPassingToResult(
        testPassingObject: TestPassingEntity,
        user: UserEntity
    ): Single<FactResult> = testDao.getTestById(testPassingObject.testId)
        .map { it.toDomain() }
        .map { clearAnswers(it) }
        .zipWith(
            getAnswersForTestPassing(testPassingObject.id),
            BiFunction<Test, List<AnswerEntity>, Test> { test, answers ->
                addAnswers(test, answers)
            })
        .map {
            FactResult(
                time = DateTime.parse(testPassingObject.date),
                user = user,
                test = it
            )
        }

    private fun addAnswers(test: Test, answers: List<AnswerEntity>): Test =
        test.copy(
            questions = test.questions.map {
                it.copy(answers = answers
                    .filter { ans -> ans.questionId == it.id }
                    .map { ans ->
                        Test.Answer(
                            ans.id,
                            ans.text,
                            ans.mark
                        )
                    })
            }
        )


    private fun getAnswersForTestPassing(passingId: String): Single<List<AnswerEntity>> =
        factAnswerDao.getAnswersForPassing(passingId)
            .flattenMap { it.answerId }
            .flattenAsFlowable { it }
            .flatMapSingle { answerDao.getAnswerById(it) }
            .toList()

    private fun clearAnswers(test: Test): Test = test.copy(
        questions = test.questions.map { it.copy(answers = emptyList()) }
    )


}