package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.QuestionDao
import javax.inject.Inject
import javax.inject.Provider

class QuestionDaoProvider @Inject constructor(private val database: AppDatabase) :
    Provider<QuestionDao> {
    override fun get(): QuestionDao = database.questionDao()
}