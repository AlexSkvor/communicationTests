package ru.lingstra.communications.domain.test_list

import io.reactivex.Observable
import ru.lingstra.communications.data.repository.SynchronizationRepository
import ru.lingstra.communications.data.repository.TestsListRepository
import ru.lingstra.communications.endWith
import javax.inject.Inject

class TestsListInteractor @Inject constructor(
    private val repository: TestsListRepository,
    private val syncRepository: SynchronizationRepository
) {

    fun synchronize(): Observable<TestsListPartialState> =
        syncRepository.loadAllTests()
            .toSingleDefault(TestsListPartialState.Loaading(false).partial())
            .toObservable()
            .startWith(TestsListPartialState.Loaading(true))
            .onErrorReturn { TestsListPartialState.Error(it) }
            .endWith(TestsListPartialState.Loaading(false))
}