package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.TestPassingDao
import javax.inject.Inject
import javax.inject.Provider

class TestPassingDaoProvider @Inject constructor(private val appDatabase: AppDatabase): Provider<TestPassingDao> {
    override fun get(): TestPassingDao = appDatabase.testPassingDao()
}