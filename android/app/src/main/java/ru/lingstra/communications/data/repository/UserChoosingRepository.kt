package ru.lingstra.communications.data.repository

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Completable
import io.reactivex.Observable
import ru.lingstra.communications.data.database.dao.UserDao
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import java.util.*
import javax.inject.Inject

interface UserChoosingRepos {
    fun loadUsers(): Observable<List<UserEntity>>
    fun deleteUser(user: UserEntity): Completable
    fun addUser(name: String): Completable
    val userAdditions: PublishRelay<Unit>
}

class UserChoosingRepository @Inject constructor(
    private val userDao: UserDao,
    private val schedulers: SchedulersProvider
) : UserChoosingRepos {

    override fun loadUsers(): Observable<List<UserEntity>> = userDao.getAll()
        .toObservable()
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    override fun deleteUser(user: UserEntity): Completable = Completable.fromAction {
        userDao.delete(user)
    }.subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    override fun addUser(name: String): Completable = Completable.fromAction {
        val newUser = UserEntity(id = UUID.randomUUID().toString(), name = name)
        userDao.insert(newUser)
    }.subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    override val userAdditions: PublishRelay<Unit> = PublishRelay.create<Unit>()

}