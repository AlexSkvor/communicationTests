package ru.lingstra.communications.data.database.dao

import ru.lingstra.communications.data.database.relations.Test
import javax.inject.Inject

class TestDaoImproved @Inject constructor(
    private val testsDao: TestDao,
    private val questionDao: QuestionDao,
    private val resultsDao: ResultReferenceDao
) {

    fun insert(test: Test){
        testsDao.insert(test.innerTest)
        resultsDao.insertAll(test.results)
        questionDao.insertQuestionsWithAnswers(test.questions)
    }

}