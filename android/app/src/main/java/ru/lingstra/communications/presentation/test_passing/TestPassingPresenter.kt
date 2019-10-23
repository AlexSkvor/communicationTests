package ru.lingstra.communications.presentation.test_passing

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.test_passing.TestPassingPartialState
import ru.lingstra.communications.domain.test_passing.TestPassingViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.NavigationManager
import ru.lingstra.communications.ui.test_passing.ARG_TAG
import javax.inject.Inject

class TestPassingPresenter @Inject constructor(
    private val navigationManager: NavigationManager
) : BaseMviPresenter<TestPassingView, TestPassingViewState>() {

    override fun bindIntents() {
        val actions = getActions().share()
        val test = navigationManager.arguments[ARG_TAG] as Test
        val initialState = TestPassingViewState(test)
        subscribeActions(actions)
        subscribeViewState(
            actions.scan(initialState, reducer).distinctUntilChanged(),
            TestPassingView::render
        )
    }

    private val reducer =
        BiFunction { oldState: TestPassingViewState, it: TestPassingPartialState ->
            when (it) {
                is TestPassingPartialState.Answer -> oldState.copy(answers = oldState.answers + it.answer)
                is TestPassingPartialState.ShowResult -> oldState
            }
        }

    private fun getActions(): Observable<TestPassingPartialState> {
        val answerAction = intent(TestPassingView::answerChosenIntent)
            .map { TestPassingPartialState.Answer(it to it.answers.first()) }

        val completeAction = intent(TestPassingView::completeIntent)
            .map { TestPassingPartialState.ShowResult }

        val list = listOf(answerAction, completeAction)
        return Observable.merge(list)
    }

    private fun subscribeActions(actions: Observable<TestPassingPartialState>) {
        actions
            .subscribe {
                when (it) {
                    is TestPassingPartialState.ShowResult -> {
                    }//TODO() navigate to other screen; save results
                }
            }.bind()
    }
}