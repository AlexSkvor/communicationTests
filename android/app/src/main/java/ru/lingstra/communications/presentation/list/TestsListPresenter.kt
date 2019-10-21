package ru.lingstra.communications.presentation.list

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.test_list.TestsListInteractor
import ru.lingstra.communications.domain.test_list.TestsListPartialState
import ru.lingstra.communications.domain.test_list.TestsListViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.ResourceManager
import ru.lingstra.communications.system.SystemMessage
import timber.log.Timber
import javax.inject.Inject

class TestsListPresenter @Inject constructor(
    private val resourceManager: ResourceManager,
    private val systemMessage: SystemMessage,
    private val interactor: TestsListInteractor
) : BaseMviPresenter<TestsListView, TestsListViewState>() {

    override fun bindIntents() {

        val actions = getActions().share()
        subscribeActions(actions)
        subscribeViewState(
            actions.scan(TestsListViewState(), reducer).distinctUntilChanged(),
            TestsListView::render
        )
    }

    private val reducer = BiFunction { oldState: TestsListViewState, it: TestsListPartialState ->
        when (it) {
            is TestsListPartialState.Error -> oldState
            is TestsListPartialState.Loaading -> oldState
        }
    }

    private fun subscribeActions(action: Observable<TestsListPartialState>) {
        action.subscribe {
            when (it) {
                is TestsListPartialState.Error -> {
                    systemMessage.send(resourceManager.getString(R.string.errorHappened))
                    Timber.e(it.error)
                }
                is TestsListPartialState.Loaading -> systemMessage.showProgress(it.loading)
            }
        }.bind()
    }

    private fun getActions(): Observable<TestsListPartialState> {
        val action = intent(TestsListView::action)
            .switchMap { interactor.synchronize() }

        val list = listOf(action)
        return Observable.merge(list)
    }
}