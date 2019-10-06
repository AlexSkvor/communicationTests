package ru.lingstra.communications.data.repository

import io.reactivex.Completable
import ru.lingstra.communications.data.database.dao.UserDao
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
    private val scheduler: SchedulersProvider
) {

    fun doSomething(): Completable =
        Completable.fromAction{
            userDao.insert(UserEntity(UUID.randomUUID().toString(), "Name", "Password_Password_29.12.1995"))
        }.subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

}