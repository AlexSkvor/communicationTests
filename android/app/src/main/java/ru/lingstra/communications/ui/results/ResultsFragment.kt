package ru.lingstra.communications.ui.results

import ru.lingstra.communications.R
import ru.lingstra.communications.domain.result.ResultsViewState
import ru.lingstra.communications.presentation.token.ResultPresenter
import ru.lingstra.communications.presentation.token.ResultView
import ru.lingstra.communications.ui.base.MviBaseFragment

class ResultsFragment : MviBaseFragment<ResultView, ResultPresenter>(), ResultView {

    override fun createPresenter(): ResultPresenter =
        scope.getInstance(ResultPresenter::class.java)

    override val layoutRes: Int
        get() = R.layout.fragment_results_list

    override fun render(state: ResultsViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}