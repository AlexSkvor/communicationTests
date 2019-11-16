package ru.lingstra.communications.presentation.test_passing

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.test_passing.TestPassingViewState

interface TestPassingView : MvpView {

    fun answerChosenIntent(): Observable<Test.Question>
    fun completeIntent(): Observable<Unit>
    fun startIntent(): Observable<Unit>

    fun render(state: TestPassingViewState)
}