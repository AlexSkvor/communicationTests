package ru.lingstra.avitocopy.data.repository

import ru.lingstra.avitocopy.data.prefs.AppPrefs
import ru.lingstra.avitocopy.system.network.NetworkApi
import ru.lingstra.avitocopy.system.schedulers.SchedulersProvider
import javax.inject.Inject

class TestsListRepository @Inject constructor(
    private val api: NetworkApi,
    private val prefs: AppPrefs,
    private val scheduler: SchedulersProvider
) {

}