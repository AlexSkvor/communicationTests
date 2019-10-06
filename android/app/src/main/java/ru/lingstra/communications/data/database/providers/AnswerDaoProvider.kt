package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.AnswerDao
import javax.inject.Inject
import javax.inject.Provider

class AnswerDaoProvider @Inject constructor(private val database: AppDatabase) :
    Provider<AnswerDao> {
    override fun get(): AnswerDao = database.answerDao()
}