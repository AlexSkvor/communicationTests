package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.TestDao
import javax.inject.Inject
import javax.inject.Provider

class TestDaoProvider @Inject constructor(private val database: AppDatabase) : Provider<TestDao> {
    override fun get(): TestDao = database.testDao()
}