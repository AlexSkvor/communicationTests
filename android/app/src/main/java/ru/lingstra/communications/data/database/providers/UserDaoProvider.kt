package ru.lingstra.communications.data.database.providers

import ru.lingstra.communications.data.database.AppDatabase
import ru.lingstra.communications.data.database.dao.UserDao
import javax.inject.Inject
import javax.inject.Provider

class UserDaoProvider @Inject constructor(private val database: AppDatabase) : Provider<UserDao> {
    override fun get(): UserDao = database.userDao()
}