package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.FactAnswerDao
import javax.inject.Inject
import javax.inject.Provider

class FactAnswerDaoProvider @Inject constructor(private val appDatabase: AppDatabase) :
    Provider<FactAnswerDao> {
    override fun get(): FactAnswerDao = appDatabase.factAnswerDao()
}