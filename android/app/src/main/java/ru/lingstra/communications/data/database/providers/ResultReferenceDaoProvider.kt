package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.ResultReferenceDao
import javax.inject.Inject
import javax.inject.Provider

class ResultReferenceDaoProvider @Inject constructor(private val database: AppDatabase) :
    Provider<ResultReferenceDao> {
    override fun get(): ResultReferenceDao = database.resultReferenceDao()
}