package ru.lingstra.communications.presentation.token

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.communications.domain.result.FactResult
import ru.lingstra.communications.domain.result.ResultsViewState

interface ResultView : MvpView {
    fun initialLoad(): Observable<Unit> = Observable.just(Unit)
    fun render(state: ResultsViewState)
}