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
import ru.lingstra.communications.ui.test_passing.ARG_TAG_TEST_PASSING_FRAGMENT
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
            is TestsListPartialState.Loading -> loading(it.loading, oldState)
            is TestsListPartialState.TestPressed -> oldState
        }
    }

    private fun loading(showLoading: Boolean, oldState: TestsListViewState): TestsListViewState{
        if (!showLoading && oldState.tests.isEmpty() && !prefs.onlyFavourites)
            navigationManager.syncPlease()
        return oldState
    }

    private fun subscribeActions(action: Observable<TestsListPartialState>) {
        action.subscribe {
            when (it) {
                is TestsListPartialState.Error -> {
                    systemMessage.send(resourceManager.getString(R.string.errorHappened))
                    Timber.e(it.error)
                }
                is TestsListPartialState.TestPressed -> {
                    navigationManager.arguments[ARG_TAG_TEST_PASSING_FRAGMENT] = it.test
                    navigationManager.navigate(R.id.testPassingFragment)
                }
                is TestsListPartialState.Loading -> systemMessage.showProgress(it.loading)
            }
        }.bind()
    }

    private fun getActions(): Observable<TestsListPartialState> {

        val favouriteAction = intent(TestsListView::favouriteClicked)
            .switchMap { interactor.markFavourite(!it.isFavourite, it.id) }

        val loadListAction = intent(TestsListView::loadListIntent)
            .switchMap { interactor.getTestsList() }

        val favouriteSettingAction = prefs.onlyFavouritesFlagChanges()
            .switchMap { interactor.getTestsList() }

        val clickedAction = intent(TestsListView::testClicked)
            .doOnNext { if (prefs.user.isEmpty()) systemMessage.send(resourceManager.getString(R.string.chooseUserBefore)) }
            .filter { prefs.user.isNotEmpty() }
            .map { TestsListPartialState.TestPressed(it) }

        val userChangedAction = prefs.userChanges()
            .switchMap { interactor.getTestsList() }

        val list = listOf(
            loadListAction,
            clickedAction,
            favouriteAction,
            favouriteSettingAction,
            userChangedAction
        )
        return Observable.merge(list)
    }
}