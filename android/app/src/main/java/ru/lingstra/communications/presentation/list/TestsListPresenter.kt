package ru.lingstra.communications.presentation.list

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.R
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.domain.test_list.TestsListInteractor
import ru.lingstra.communications.domain.test_list.TestsListPartialState
import ru.lingstra.communications.domain.test_list.TestsListViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.NavigationManager
import ru.lingstra.communications.system.ResourceManager
import ru.lingstra.communications.system.SystemMessage
import ru.lingstra.communications.ui.test_passing.ARG_TAG
import timber.log.Timber
import javax.inject.Inject

class TestsListPresenter @Inject constructor(
    private val resourceManager: ResourceManager,
    private val systemMessage: SystemMessage,
    private val navigationManager: NavigationManager,
    private val interactor: TestsListInteractor,
    private val prefs: AppPrefs
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
            is TestsListPartialState.TestsList -> oldState.copy(tests = it.tests)
            is TestsListPartialState.Error -> oldState
            is TestsListPartialState.Loading -> oldState
            is TestsListPartialState.TestPressed -> oldState
        }
    }

    private fun subscribeActions(action: Observable<TestsListPartialState>) {
        action.subscribe {
            when (it) {
                is TestsListPartialState.Error -> {
                    systemMessage.send(resourceManager.getString(R.string.errorHappened))
                    Timber.e(it.error)
                }
                is TestsListPartialState.TestPressed -> {
                    navigationManager.arguments[ARG_TAG] = it.test
                    navigationManager.navigate(R.id.testPassingFragment)
                }
                is TestsListPartialState.Loading -> systemMessage.showProgress(it.loading)
            }
        }.bind()
    }

    private fun getActions(): Observable<TestsListPartialState> {
        val syncAction = intent(TestsListView::syncIntent)
            .switchMap { interactor.synchronize().concatWith(interactor.getTestsList()) }

        val loadListAction = intent(TestsListView::loadListIntent)
            .switchMap { interactor.getTestsList() }

        val clickedAction = intent(TestsListView::testClicked)
            .doOnNext { if (prefs.user.isEmpty()) systemMessage.send(resourceManager.getString(R.string.chooseUserBefore)) }
            .filter { prefs.user.isNotEmpty() }
            .map { TestsListPartialState.TestPressed(it) }

        val list = listOf(syncAction, loadListAction, clickedAction)
        return Observable.merge(list)
    }
}