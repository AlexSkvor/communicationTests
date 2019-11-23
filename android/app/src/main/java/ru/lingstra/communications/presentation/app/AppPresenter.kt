package ru.lingstra.communications.presentation.app

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.R
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.domain.app.AppInteractor
import ru.lingstra.communications.domain.app.AppViewState
import ru.lingstra.communications.domain.app.AppPartialState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.NavigationManager
import ru.lingstra.communications.system.ResourceManager
import ru.lingstra.communications.system.SystemMessage
import timber.log.Timber
import javax.inject.Inject

class AppPresenter @Inject constructor(
    private val systemMessage: SystemMessage,
    private val navigationManager: NavigationManager,
    private val prefs: AppPrefs,
    private val interactor: AppInteractor,
    private val resourceManager: ResourceManager
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
            is AppPartialState.Error -> {
                Timber.e(it.error)
                oldState.copy(
                    toast = resourceManager.getString(R.string.errorHappened),
                    render = AppViewState.Render.TOAST
                )
            }
            is AppPartialState.Loading -> oldState.copy(
                progress = it.loading,
                render = AppViewState.Render.PROGRESS
            )
        }
    }

    private fun getActions(): Observable<AppPartialState> {

        intent(AppView::onlyFavourites)
            .subscribe { prefs.onlyFavourites = !prefs.onlyFavourites }.bind()

        intent(AppView::changeUser)
            .subscribe { navigationManager.navigate(R.id.userChoosingFragment) }.bind()

        intent(AppView::actionBack)
            .subscribe { navigationManager.back() }.bind()

        val toast = systemMessage.notifier
            .filter { it.type is SystemMessage.Type.Toast }
            .map { AppPartialState.Toast(it.text) }

        val progress = systemMessage.notifier
            .filter { it.type is SystemMessage.Type.Progress }
            .map { AppPartialState.Progress(it.progress) }

        val refresh = intent(AppView::refresh)
            .map { AppPartialState.Refresh }

        val syncIntent = Observable.merge(intent(AppView::syncIntent), navigationManager.syncActions)

        val syncAction = syncIntent
            .switchMap { interactor.synchronize() }
            .doOnNext {
                if (it is AppPartialState.Loading && !it.loading)
                    prefs.user = prefs.user
            }//dirty hack it will cause reload on all screens

        val navigation = navigationManager.actions
            .map { AppPartialState.Navigation(it) }

        val actions = listOf(refresh, toast, progress, navigation, syncAction)

        return Observable.merge(actions).share()
    }
}