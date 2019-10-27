package ru.lingstra.communications.domain.test_passing

import io.reactivex.Observable
import ru.lingstra.communications.data.repository.TestPassingRepository
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.endWith
import javax.inject.Inject

class TestPassingInteractor @Inject constructor(
    private val repository: TestPassingRepository
) {

    fun saveResult(answers: List<Test.Question>, test: Test): Observable<TestPassingPartialState> =
        repository.saveResults(answers, test.id)
            .toSingleDefault(Unit)
            .toObservable()
            .flatMap { computeResult(answers, test) }
            .map { TestPassingPartialState.ShowResult(it).partial() }
            .startWith(TestPassingPartialState.Loading(true))
            .onErrorReturn { TestPassingPartialState.Error(it) }
            .endWith(TestPassingPartialState.Loading(false))

    private fun computeResult(answers: List<Test.Question>, test: Test): Observable<Test.Result> {
        val mark = answers.map { it.answers.first() }.map { it.mark }.sum()
        test.results.forEach {
            if (mark in it.minMark..it.maxMark) return Observable.just(it)
        }
        return Observable.error(IllegalArgumentException("Получена оценка $mark. Ни один Ответ ее не предусматриввает"))
    }

}