package ru.lingstra.communications.presentation.test_passing

import com.hannesdorfmann.mosby3.mvp.MvpView
import ru.lingstra.communications.domain.test_passing.TestPassingViewState

interface TestPassingView : MvpView {

    fun render(state: TestPassingViewState)
}