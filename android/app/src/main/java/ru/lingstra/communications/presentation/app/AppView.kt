package ru.lingstra.communications.presentation.app

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.communications.domain.app.AppViewState

interface AppView : MvpView {
    fun refresh(): Observable<Unit>
    fun syncIntent(): Observable<Unit>
    fun onlyFavourites(): Observable<Unit>
    fun changeUser(): Observable<Unit>
    fun actionBack(): Observable<Unit>

    fun render(state: AppViewState)
}