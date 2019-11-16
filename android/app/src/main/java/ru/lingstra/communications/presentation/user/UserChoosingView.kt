package ru.lingstra.communications.presentation.user

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.domain.user.UserChoosingViewState

interface UserChoosingView : MvpView {
    fun init(): Observable<Unit> = Observable.just(Unit)
    fun deleteUser(): Observable<UserEntity>

    fun render(state: UserChoosingViewState)
}