package ru.lingstra.communications.ui.list

import ru.lingstra.communications.R
import ru.lingstra.communications.domain.hand_shakes.TestsListViewState
import ru.lingstra.communications.presentation.list.TestsListPresenter
import ru.lingstra.communications.presentation.list.TestsListView
import ru.lingstra.communications.ui.base.MviBaseFragment
import toothpick.Scope
import toothpick.config.Module

class TestsListFragment : MviBaseFragment<TestsListView, TestsListPresenter>(), TestsListView {

    override val layoutRes: Int
        get() = R.layout.fragment_tests_list

    override fun createPresenter(): TestsListPresenter =
        scope.getInstance(TestsListPresenter::class.java)

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                //TODO
            }
        })
    }

    override fun render(state: TestsListViewState) {

    }
}