package ru.lingstra.communications.presentation.token

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.R
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.domain.result.ResultsInteractor
import ru.lingstra.communications.domain.result.ResultsPartialState
import ru.lingstra.communications.domain.result.ResultsViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.ResourceManager
import ru.lingstra.communications.system.SystemMessage
import timber.log.Timber
import javax.inject.Inject

class ResultPresenter @Inject constructor(
    private val prefs: AppPrefs,
    private val systemMessage: SystemMessage,
    private val resourceManager: ResourceManager,
    private val interactor: ResultsInteractor
) : BaseMviPresenter<ResultView, ResultsViewState>() {

    override fun bindIntents() {
        val actions = getActions().share()
        subscribeActions(actions)
        subscribeViewState(
            actions.scan(ResultsViewState(), reducer).distinctUntilChanged(),
            ResultView::render
        )
    }

    private val reducer = BiFunction { oldState: ResultsViewState, it: ResultsPartialState ->
        when (it) {
            is ResultsPartialState.ResultsList -> oldState.copy(results = it.results)
            is ResultsPartialState.Error -> oldState
            is ResultsPartialState.Loading -> oldState
        }
    }

    private fun subscribeActions(action: Observable<ResultsPartialState>) {
        action.subscribe {
            when (it) {
                is ResultsPartialState.Error -> {
                    systemMessage.send(resourceManager.getString(R.string.errorHappened))
                    Timber.e(it.error)
                }
                is ResultsPartialState.Loading -> systemMessage.showProgress(it.loading)
            }
        }.bind()
    }

    private fun getActions(): Observable<ResultsPartialState> {
        val loadAction = intent(ResultView::initialLoad)
            .doOnNext { if (prefs.user.isEmpty()) systemMessage.send(resourceManager.getString(R.string.chooseUserBefore)) }
            .filter { prefs.user.isNotEmpty() }
            .switchMap { interactor.getResults() }

        val favouriteSettingAction = prefs.onlyFavouritesFlagChanges()
            .switchMap { interactor.getResults() }

        val userChangedAction = prefs.userChanges()
            .switchMap { interactor.getResults() }

        val list =
            listOf(loadAction, userChangedAction, favouriteSettingAction)
        return Observable.merge(list)
    }
}