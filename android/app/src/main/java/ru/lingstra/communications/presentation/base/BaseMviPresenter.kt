package ru.lingstra.communications.presentation.base

import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import ru.lingstra.communications.system.disposables.AppDisposables
import ru.lingstra.communications.system.disposables.DisposablesProvider

abstract class BaseMviPresenter<V : MvpView, VM> : MviBasePresenter<V, VM>(),
    DisposablesProvider by AppDisposables() {
    override fun unbindIntents() {
        super.unbindIntents()
        clear()
    }
}