package ru.lingstra.communications.ui.test_passing

import ru.lingstra.communications.R
import ru.lingstra.communications.domain.test_passing.TestPassingViewState
import ru.lingstra.communications.presentation.test_passing.TestPassingPresenter
import ru.lingstra.communications.presentation.test_passing.TestPassingView
import ru.lingstra.communications.ui.base.MviBaseFragment

const val ARG_TAG: String = "TestPassingFragment"
class TestPassingFragment : MviBaseFragment<TestPassingView, TestPassingPresenter>(),
    TestPassingView {

    override val layoutRes: Int
        get() = R.layout.fragment_test_passing

    override fun createPresenter(): TestPassingPresenter =
        scope.getInstance(TestPassingPresenter::class.java)

    override fun render(state: TestPassingViewState) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}