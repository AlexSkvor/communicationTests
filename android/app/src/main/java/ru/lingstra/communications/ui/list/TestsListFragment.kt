package ru.lingstra.communications.ui.list

import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tests_list.*
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.test_list.TestsListViewState
import ru.lingstra.communications.presentation.list.TestsListPresenter
import ru.lingstra.communications.presentation.list.TestsListView
import ru.lingstra.communications.ui.base.MviBaseFragment

class TestsListFragment : MviBaseFragment<TestsListView, TestsListPresenter>(), TestsListView {

    override val layoutRes: Int
        get() = R.layout.fragment_tests_list

    override fun createPresenter(): TestsListPresenter =
        scope.getInstance(TestsListPresenter::class.java)

    override fun action(): Observable<Unit> = start.clicks()

    override fun render(state: TestsListViewState) {

    }
}