package ru.lingstra.communications.presentation.app

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.domain.app.AppViewState
import ru.lingstra.communications.domain.app.AppPartialState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.NavigationManager
import ru.lingstra.communications.system.SystemMessage
import javax.inject.Inject

class AppPresenter @Inject constructor(
    private val systemMessage: SystemMessage,
    private val navigationManager: NavigationManager
) : BaseMviPresenter<AppView, AppViewState>() {
    override fun bindIntents() {
        val initialState = AppViewState()

        val actions = getActions()

        subscribeViewState(
            actions.scan(initialState, reducer).distinctUntilChanged(),
            AppView::render
        )
    }

    private val reducer = BiFunction { oldState: AppViewState, it: AppPartialState ->
        when (it) {
            is AppPartialState.Progress -> oldState.copy(
                progress = it.value,
                render = AppViewState.Render.PROGRESS
            )
            is AppPartialState.Toast -> oldState.copy(
                toast = it.message,
                render = AppViewState.Render.TOAST
            )
            is AppPartialState.Navigation -> oldState.copy(
                navigationAction = it.action,
                render = AppViewState.Render.NAVIGATION
            )
            is AppPartialState.Refresh -> oldState.copy(render = AppViewState.Render.NONE)
        }
    }

    private fun getActions(): Observable<AppPartialState> {

        val toast = systemMessage.notifier
            .filter { it.type is SystemMessage.Type.Toast }
            .map { AppPartialState.Toast(it.text) }

        val progress = systemMessage.notifier
            .filter { it.type is SystemMessage.Type.Progress }
            .map { AppPartialState.Progress(it.progress) }

        val refresh = intent(AppView::refresh)
            .map { AppPartialState.Refresh }

        val navigation = navigationManager.actions
            .map { AppPartialState.Navigation(it) }

        val actions = listOf(refresh, toast, progress, navigation)

        return Observable.merge(actions).share()
    }
}