package ru.lingstra.communications.ui.list

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tests_list.*
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.test_list.TestsListViewState
import ru.lingstra.communications.presentation.list.TestsListPresenter
import ru.lingstra.communications.presentation.list.TestsListView
import ru.lingstra.communications.ui.base.MviBaseFragment
import ru.lingstra.communications.ui.utils.delegate.CompositeDelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.pressedItems

class TestsListFragment : MviBaseFragment<TestsListView, TestsListPresenter>(), TestsListView {

    override val layoutRes: Int
        get() = R.layout.fragment_tests_list

    override fun createPresenter(): TestsListPresenter =
        scope.getInstance(TestsListPresenter::class.java)

    private val syncRelay = PublishRelay.create<Unit>()
    override fun syncIntent(): Observable<Unit> = syncRelay.hide()

    private val loadListRelay = BehaviorRelay.create<Unit>()
    override fun loadListIntent(): Observable<Unit> = loadListRelay.hide()

    private lateinit var testsAdapter: CompositeDelegateAdapter<Test>

    override fun render(state: TestsListViewState) {
        testsAdapter.replaceData(state.tests)
    }

    override fun testClicked(): Observable<Test> = testsAdapter.actions.pressedItems()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        testsAdapter = CompositeDelegateAdapter.Companion.Builder<Test>()
            .add(TestsListAdapter())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        testsRecycler.layoutManager = LinearLayoutManager(requireContext())
        testsRecycler.adapter = testsAdapter
        loadListRelay.accept(Unit)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_syncronize, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.sync -> {
                syncRelay.accept(Unit)
                true
            }
            R.id.exitUser -> {
                activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}