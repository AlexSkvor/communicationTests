package ru.lingstra.communications.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.data.database.dao.FavouriteDao
import ru.lingstra.communications.data.database.dao.TestDao
import ru.lingstra.communications.data.database.entities.FavouriteEntity
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.flattenMap
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import javax.inject.Inject

class TestsListRepository @Inject constructor(
    private val testsDao: TestDao,
    private val scheduler: SchedulersProvider,
    private val prefs: AppPrefs,
    private val favouriteDao: FavouriteDao
) {

    fun getTestsList(): Observable<List<Test>> =
        testsDao.getAllTests()
            .flattenMap { it.toDomain() }
            .zipWith(
                favouriteList(),
                BiFunction<List<Test>, List<String>, List<Test>> { tests, favourites ->
                    tests.map { if (it.id in favourites) it.copy(isFavourite = true) else it }
                })
            .map { it.filter { test -> !prefs.onlyFavourites || test.isFavourite } }
            .map { it.sortedBy { test -> test.name } }
            .toObservable()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

    private fun favouriteList(): Single<List<String>> =
        favouriteDao.getUsersFavouritesList(prefs.user.id)

    fun markFavourite(isFavourite: Boolean, testId: String): Completable =
        Completable.fromAction {
            if (isFavourite && !prefs.user.isEmpty()) favouriteDao.insert(
                FavouriteEntity(
                    prefs.user.id,
                    testId
                )
            )
            else favouriteDao.delete(FavouriteEntity(prefs.user.id, testId))
        }.subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
}