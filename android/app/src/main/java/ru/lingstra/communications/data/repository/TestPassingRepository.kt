package ru.lingstra.communications.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime
import ru.lingstra.communications.data.database.dao.FactAnswerDao
import ru.lingstra.communications.data.database.dao.TestPassingDao
import ru.lingstra.communications.data.database.entities.FactAnswerEntity
import ru.lingstra.communications.data.database.entities.TestPassingEntity
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.system.DATE_MASK
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import java.util.*
import javax.inject.Inject

class TestPassingRepository @Inject constructor(
    private val factAnswerDao: FactAnswerDao,
    private val testPassingDao: TestPassingDao,
    private val prefs: AppPrefs,
    private val scheduler: SchedulersProvider
) {

    fun saveResults(answers: List<Test.Question>, testId: String): Completable =
        createTestPassing(testId)
            .doOnSuccess { testPassingDao.insert(it) }
            .flatMapCompletable { saveAnswers(answers, it.id) }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

    private fun saveAnswers(answers: List<Test.Question>, testPassingId: String): Completable =
        Single.just(answers)
            .flattenAsFlowable { it }
            .flatMapCompletable { saveAnswer(it, testPassingId) }

    private fun saveAnswer(answer: Test.Question, testPassingId: String): Completable =
        Completable.fromAction {
            factAnswerDao.insert(
                FactAnswerEntity(passingId = testPassingId, answerId = answer.answers.first().id!!)
            )
        }

    private fun createTestPassing(testId: String): Single<TestPassingEntity> =
        Single.just(
            TestPassingEntity(
                id = UUID.randomUUID().toString(),
                userId = prefs.userId,
                testId = testId,
                date = DateTime.now().toString(DATE_MASK)
            )
        )

}