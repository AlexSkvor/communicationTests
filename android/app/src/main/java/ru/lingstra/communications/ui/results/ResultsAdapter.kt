package ru.lingstra.communications.ui.results

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_fact_test_result.view.*
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.result.FactResult
import ru.lingstra.communications.ui.utils.delegate.DelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.UserAction

class ResultsAdapter : DelegateAdapter<FactResult>() {

    private val actionsRelay = PublishRelay.create<UserAction<FactResult>>()
    override fun getAction(): Observable<UserAction<FactResult>> = actionsRelay.hide()

    override fun onBind(item: FactResult, holder: DelegateViewHolder) = with(holder.itemView) {
        testName.text = item.test.name
        resultText.text = item.resultText
        passingDate.text = resources.getString(R.string.passingDate, item.time.toString("DD.MM.YYY HH:mm"))
    }

    override fun getLayoutId(): Int = R.layout.item_fact_test_result

    override fun isForViewType(items: List<*>, position: Int): Boolean =
        items[position] is FactResult
}