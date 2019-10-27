package ru.lingstra.communications.presentation.test_passing

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.test_passing.TestPassingInteractor
import ru.lingstra.communications.domain.test_passing.TestPassingPartialState
import ru.lingstra.communications.domain.test_passing.TestPassingViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.NavigationManager
import ru.lingstra.communications.system.SystemMessage
import ru.lingstra.communications.ui.test_passing.ARG_TAG
import timber.log.Timber
import javax.inject.Inject

class TestPassingPresenter @Inject constructor(
    private val systemMessage: SystemMessage,
    private val navigationManager: NavigationManager,
    private val interactor: TestPassingInteractor
) : BaseMviPresenter<TestPassingView, TestPassingViewState>() {
    //TODO скрывать кнопку завершить и показывать кнопку выйти после получения результата
    override fun bindIntents() {
        val actions = getActions().share()
        val test = navigationManager.arguments[ARG_TAG] as Test
        test.questions.forEach {
            it.answers.forEach { ans -> ans.chosen = false }
        }
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
                is TestPassingPartialState.ShowResult -> oldState.copy(result = it.result)
                is TestPassingPartialState.Loading -> oldState
                is TestPassingPartialState.Error -> oldState
            }
        }

    private fun getActions(): Observable<TestPassingPartialState> {
        val answerAction = intent(TestPassingView::answerChosenIntent)
            .map { TestPassingPartialState.Answer(it.id!! to it) }

        val completeAction = intent(TestPassingView::completeIntent)
            .switchMap { viewStateObservable.take(1) }
            .switchMap { interactor.saveResult(it.answers.values.toList(), it.test) }

        val list = listOf(answerAction, completeAction)
        return Observable.merge(list)
    }

    private fun subscribeActions(actions: Observable<TestPassingPartialState>) {
        actions.subscribe {
            when (it) {
                is TestPassingPartialState.Loading -> systemMessage.showProgress(it.loading)
                is TestPassingPartialState.Error -> {
                    systemMessage.send(it.t.message ?: "")
                    Timber.e(it.t)
                }
            }
        }.bind()
    }
}