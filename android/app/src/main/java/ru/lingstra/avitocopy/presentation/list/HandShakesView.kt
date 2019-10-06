package ru.lingstra.avitocopy.presentation.list

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.avitocopy.domain.hand_shakes.HandShakesViewState

interface HandShakesView: MvpView {
    fun startSearch(): Observable<Pair<String, String>>

    fun render(state: HandShakesViewState)
}