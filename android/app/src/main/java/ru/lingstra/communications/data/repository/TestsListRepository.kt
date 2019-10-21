package ru.lingstra.communications.data.repository

import io.reactivex.Observable
import ru.lingstra.communications.data.database.dao.TestDao
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.flattenMap
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import javax.inject.Inject

class TestsListRepository @Inject constructor(
    private val testsDao: TestDao,
    private val scheduler: SchedulersProvider
) {

    fun getTestsList(): Observable<List<Test>> =
        testsDao.getAllTests()
            .flattenMap { it.toDomain() }
            .toObservable()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
}