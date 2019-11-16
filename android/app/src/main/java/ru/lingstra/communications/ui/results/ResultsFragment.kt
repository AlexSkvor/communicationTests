package ru.lingstra.communications.ui.results

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_results_list.*
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.result.FactResult
import ru.lingstra.communications.domain.result.ResultsViewState
import ru.lingstra.communications.presentation.token.ResultPresenter
import ru.lingstra.communications.presentation.token.ResultView
import ru.lingstra.communications.ui.base.MviBaseFragment
import ru.lingstra.communications.ui.utils.ItemDecoration
import ru.lingstra.communications.ui.utils.delegate.CompositeDelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.pressedItems

class ResultsFragment : MviBaseFragment<ResultView, ResultPresenter>(), ResultView {

    private lateinit var adapter: CompositeDelegateAdapter<FactResult>

    override fun createPresenter(): ResultPresenter =
        scope.getInstance(ResultPresenter::class.java)

    override val layoutRes: Int
        get() = R.layout.fragment_results_list

    override fun render(state: ResultsViewState) {
        adapter.replaceData(state.results)
    }

    override fun resultClicked(): Observable<FactResult> = adapter.actions.pressedItems()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = CompositeDelegateAdapter.Companion.Builder<FactResult>()
            .add(ResultsAdapter())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        resultsRecycler.layoutManager = LinearLayoutManager(requireContext())
        val space = resources.getDimensionPixelSize(R.dimen.marginNormal)
        resultsRecycler.addItemDecoration(ItemDecoration(space))
        resultsRecycler.adapter = adapter
    }

}