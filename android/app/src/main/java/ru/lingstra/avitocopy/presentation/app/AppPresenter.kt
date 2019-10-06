package ru.lingstra.avitocopy.presentation.app

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.avitocopy.domain.app.AppViewState
import ru.lingstra.avitocopy.domain.app.AppPartialState
import ru.lingstra.avitocopy.presentation.base.BaseMviPresenter
import ru.lingstra.avitocopy.system.SystemMessage
import javax.inject.Inject

class AppPresenter @Inject constructor(
    private val systemMessage: SystemMessage
) : BaseMviPresenter<AppView, AppViewState>() {
    override fun bindIntents() {
        val initialState = AppViewState()

        val actions = getActions()

        subscribeViewState(
            actions.scan(initialState, reducer).distinctUntilChanged(),
            AppView::render
        )
    }

    private val reducer = BiFunction { oldState: AppViewState, partial: AppPartialState ->
        when (partial) {
            is AppPartialState.Progress -> oldState.copy(progress = partial.value, render = AppViewState.Render.PROGRESS)
            is AppPartialState.Toast -> oldState.copy(toast = partial.message, render = AppViewState.Render.TOAST)
            else -> oldState.copy(render = AppViewState.Render.NONE)
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

        val actions = listOf(refresh, toast, progress)

        return Observable.merge(actions).share()
    }
}