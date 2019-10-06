package ru.lingstra.avitocopy.ui.list

import ru.lingstra.avitocopy.R
import ru.lingstra.avitocopy.domain.hand_shakes.TestsListViewState
import ru.lingstra.avitocopy.presentation.list.TestsListPresenter
import ru.lingstra.avitocopy.presentation.list.TestsListView
import ru.lingstra.avitocopy.ui.base.MviBaseFragment
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