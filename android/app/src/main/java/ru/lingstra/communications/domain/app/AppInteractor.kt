package ru.lingstra.communications.domain.app

import io.reactivex.Observable
import ru.lingstra.communications.data.repository.SynchronizationRepository
import ru.lingstra.communications.endWith
import javax.inject.Inject

class AppInteractor @Inject constructor(
    private val repository: SynchronizationRepository
) {

    fun synchronize(): Observable<AppPartialState> =
        repository.loadAllTests()
            .toSingleDefault(AppPartialState.Loading(false).partial())
            .toObservable()
            .startWith(AppPartialState.Loading(true))
            .onErrorReturn { AppPartialState.Error(it) }
            .endWith(AppPartialState.Loading(false))
}