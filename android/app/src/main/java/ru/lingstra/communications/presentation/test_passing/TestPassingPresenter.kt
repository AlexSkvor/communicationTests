package ru.lingstra.communications.presentation.test_passing

import ru.lingstra.communications.alsoPrintDebug
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.test_passing.TestPassingViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.NavigationManager
import ru.lingstra.communications.ui.test_passing.ARG_TAG
import javax.inject.Inject

class TestPassingPresenter @Inject constructor(
    private val navigationManager: NavigationManager
) : BaseMviPresenter<TestPassingView, TestPassingViewState>() {

    override fun bindIntents() {
        val test = navigationManager.arguments[ARG_TAG] as Test
        val initialState = TestPassingViewState(test)
    }
}