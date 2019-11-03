package ru.lingstra.communications.domain.result

import io.reactivex.Observable
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.data.repository.ResultsRepository
import ru.lingstra.communications.endWith
import javax.inject.Inject

class ResultsInteractor @Inject constructor(
    private val repository: ResultsRepository
) {

    fun getResults(): Observable<ResultsPartialState> =
        repository.getResults()
            .map { ResultsPartialState.ResultsList(it).partial() }
            .startWith(ResultsPartialState.Loading(true))
            .onErrorReturn { ResultsPartialState.Error(it) }
            .endWith(ResultsPartialState.Loading(false))

}