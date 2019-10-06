package ru.lingstra.avitocopy.presentation.app

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.avitocopy.domain.app.AppViewState

interface AppView: MvpView {
    fun refresh(): Observable<Unit>

    fun render(state: AppViewState)
}