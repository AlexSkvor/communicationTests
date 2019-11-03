package ru.lingstra.communications.domain.test_list

import io.reactivex.Observable
import ru.lingstra.communications.data.repository.TestsListRepository
import ru.lingstra.communications.endWith
import javax.inject.Inject

class TestsListInteractor @Inject constructor(
    private val repository: TestsListRepository
) {

    fun getTestsList(): Observable<TestsListPartialState> = repository.getTestsList()
        .map { TestsListPartialState.TestsList(it).partial() }
        .startWith(TestsListPartialState.Loading(true))
        .onErrorReturn { TestsListPartialState.Error(it) }
        .endWith(TestsListPartialState.Loading(false))

    fun markFavourite(isFavourite: Boolean, testId: String): Observable<TestsListPartialState> =
        repository.markFavourite(isFavourite, testId)
            .andThen(getTestsList())
}