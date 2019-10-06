package ru.lingstra.communications.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.lingstra.communications.alsoPrintDebug
import ru.lingstra.communications.data.database.dao.QuestionDao
import ru.lingstra.communications.data.database.dao.TestDao
import ru.lingstra.communications.data.database.dao.UserDao
import ru.lingstra.communications.data.database.entities.AnswerEntity
import ru.lingstra.communications.data.database.entities.QuestionEntity
import ru.lingstra.communications.data.database.entities.TestEntity
import ru.lingstra.communications.data.database.relations.QuestionWithAnswers
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.system.network.NetworkApi
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import java.util.*
import javax.inject.Inject

class TestsListRepository @Inject constructor(
    private val api: NetworkApi,
    private val prefs: AppPrefs,
    private val userDao: UserDao,
    private val testsDao: TestDao,
    private val questionDao: QuestionDao,
    private val scheduler: SchedulersProvider
) {

    private val uuid: String
        get() = UUID.randomUUID().toString()

    fun doSomething(): Completable =
        Single.just(uuid)
            .map { TestEntity(id = it, name = "new test", description = "What?! No!") }
            .doOnSuccess { testsDao.insert(it) }
            .map { question(it.id) }
            .doOnSuccess { questionDao.insertQuestionWithAnswers(it) }
            .flatMapCompletable { readQuestionCompletable(it.question.id) }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

    private fun readQuestionCompletable(questionId: String): Completable =
        questionDao.getQuestionWithAnswersById(questionId)
            .doOnSuccess { it.alsoPrintDebug("AAAAAAAAAAAA") }
            .doOnSuccess { it.answers.alsoPrintDebug("BBBBBBBBBBBB") }
            .ignoreElement()

    private fun question(testId: String): QuestionWithAnswers {
        val question = QuestionEntity(id = uuid, testId = testId, text = "Че каво?!")
        val answers = mutableListOf<AnswerEntity>()
        for (i in 0..3) {
            answers.add(
                AnswerEntity(id = uuid, questionId = question.id, text = "Гы гы гы $i", mark = i)
            )
        }

        val res = QuestionWithAnswers(question)
        res.answers = answers
        return res
    }

}