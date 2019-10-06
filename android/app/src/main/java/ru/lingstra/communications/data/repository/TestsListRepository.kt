package ru.lingstra.communications.data.repository

import android.os.Environment
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import ru.lingstra.communications.alsoPrintDebug
import ru.lingstra.communications.data.database.dao.QuestionDao
import ru.lingstra.communications.data.database.dao.TestDao
import ru.lingstra.communications.data.database.dao.TestDaoImproved
import ru.lingstra.communications.data.database.dao.UserDao
import ru.lingstra.communications.data.database.entities.AnswerEntity
import ru.lingstra.communications.data.database.entities.QuestionEntity
import ru.lingstra.communications.data.database.entities.ResultReferenceEntity
import ru.lingstra.communications.data.database.entities.TestEntity
import ru.lingstra.communications.data.database.relations.QuestionWithAnswers
import ru.lingstra.communications.data.database.relations.toData
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.system.network.NetworkApi
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import ru.lingstra.communications.toothpick.DefaultFilesDir
import java.io.File
import java.util.*
import javax.inject.Inject

class TestsListRepository @Inject constructor(
    private val testsDao: TestDao,
    private val questionDao: QuestionDao,
    private val scheduler: SchedulersProvider,
    private val testDaoImproved: TestDaoImproved,
    @DefaultFilesDir private val filesDir: String
) {

    private val uuid: String
        get() = UUID.randomUUID().toString()

    fun doSomething(): Completable =
        /*Single.just(uuid)
            .map { TestEntity(id = it, name = "new text", description = "What?! No!") }
            .doOnSuccess { testsDao.insert(it) }
            .map { question(it.id) }
            .doOnSuccess { questionDao.insertQuestionWithAnswers(it) }
            .flatMapCompletable { readQuestionCompletable(it.question.id) }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())*/

        Single.just(generateTest())
            .doOnSuccess { writeTest(it) }
            .doOnSuccess { testDaoImproved.insert(it.toEntity()) }
            .ignoreElement()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

    private fun Test.toEntity(): ru.lingstra.communications.data.database.relations.Test {
        val r: List<Test.Result> = this.results
        val q: List<Test.Question> = this.questions
        return ru.lingstra.communications.data.database.relations.Test(
            innerTest = TestEntity(
                id,
                name,
                description
            )
        ).apply {
            results = r.map {
                ResultReferenceEntity(
                    id = uuid,
                    testId = innerTest.id,
                    minMark = it.minMark,
                    maxMark = it.maxMark,
                    resultDescription = it.text
                )
            }
            questions = q.toData(innerTest.id)
        }
    }

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

    private fun generateTest(): Test = Test(
        id = uuid,
        name = "Тест для примера",
        description = "Описание теста в нескольких предложениях",
        questions = listOf(generateQuestion()),
        results = listOf(generateResult())
    )

    private fun generateQuestion(): Test.Question = Test.Question(
        text = "Текст вопроса",
        answers = listOf(generateAnswer(1), generateAnswer(2), generateAnswer(3), generateAnswer(4))
    )

    private fun generateAnswer(mark: Int): Test.Answer = Test.Answer(
        text = "Текст ответа",
        mark = mark
    )

    private fun generateResult() = Test.Result(
        minMark = 0,
        maxMark = 20,
        text = "Текст результата теста на случай, если тестируемый набрал 20 или менее баллов. Количество балло для каждого ответа на каждый вопрос задается отдельно!"
    )

    private fun writeTest(test: Test, fileName: String = "test") {
        val directory = File(filesDir + File.separator)
        if (!directory.exists()) directory.mkdirs()
        val file = File(filesDir + File.separator + fileName + ".json")
        val t = Gson().toJson(test)
        t.alsoPrintDebug("AAAAAAAAAAA")
        file.writeText(t)
    }

    private fun readTest(): Test {
        TODO()
    }

    private fun getTest(): Single<Test> {
        TODO()
    }

}