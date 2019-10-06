package ru.lingstra.communications.data.repository

import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.system.network.NetworkApi
import ru.lingstra.communications.system.schedulers.SchedulersProvider
import javax.inject.Inject

class TestsListRepository @Inject constructor(
    private val api: NetworkApi,
    private val prefs: AppPrefs,
    private val scheduler: SchedulersProvider
) {

}