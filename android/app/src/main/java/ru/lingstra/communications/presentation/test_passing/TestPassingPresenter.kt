package ru.lingstra.communications.presentation.test_passing

import ru.lingstra.communications.domain.test_passing.TestPassingViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import javax.inject.Inject

class TestPassingPresenter @Inject constructor(

) : BaseMviPresenter<TestPassingView, TestPassingViewState>() {

    override fun bindIntents() {

    }
}