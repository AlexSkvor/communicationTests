package ru.lingstra.communications.data.repository

import io.reactivex.Completable
import ru.lingstra.communications.data.database.dao.TestDao
import ru.lingstra.communications.data.database.dao.UserDao
import ru.lingstra.communications.data.database.entities.TestEntity
import ru.lingstra.communications.data.database.entities.UserEntity
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
    private val scheduler: SchedulersProvider
) {

    private val uuid: String
        get() = UUID.randomUUID().toString()

    fun doSomething(): Completable =
        Completable.fromAction {
            userDao.insert(UserEntity(id = uuid, name = "Name", password = "Password"))
            testsDao.insert(TestEntity(id = uuid, name = "new test", description = "What?! No!"))
        }.subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

}