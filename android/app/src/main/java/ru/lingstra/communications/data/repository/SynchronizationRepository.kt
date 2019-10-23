package ru.lingstra.communications.data.repository

import com.google.gson.Gson
import io.reactivex.Completable
import ru.lingstra.communications.alsoPrintDebug
import ru.lingstra.communications.data.data_entities.FileList
import ru.lingstra.communications.data.database.dao.TestDaoImproved
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.system.network.NetworkApi
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import timber.log.Timber
import javax.inject.Inject

//TODO in future consider checking for changed date of every test!
class SynchronizationRepository @Inject constructor(
    private val api: NetworkApi,
    private val dao: TestDaoImproved,
    private val scheduler: SchedulersProvider
) {

    fun loadAllTests(): Completable =
        api.getFile("tests_list")
            .map { it.fileText }
            .map { Gson().fromJson(it, FileList::class.java) }
            .map { it.list }
            .flattenAsFlowable { it }
            .flatMapCompletable { loadTest(it) }
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())

    private fun loadTest(fileName: String) =
        api.getFile(fileName)
            .map { it.fileText }
            .map { Gson().fromJson(it, Test::class.java) }
            .map { it.toEntity() }
            .doOnSuccess { dao.insert(it) }
            .doOnError { Timber.e(it) }
            .ignoreElement()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
}