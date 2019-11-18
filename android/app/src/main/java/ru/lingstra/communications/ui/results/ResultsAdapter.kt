package ru.lingstra.communications.ui.results

import kotlinx.android.synthetic.main.item_fact_test_result.view.*
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.result.FactResult
import ru.lingstra.communications.ui.utils.delegate.DelegateAdapter

class ResultsAdapter : DelegateAdapter<FactResult>() {

    override fun onBind(item: FactResult, holder: DelegateViewHolder) = with(holder.itemView) {
        testName.text = item.test.name
        resultText.text = item.resultText
        passingDate.text =
            resources.getString(R.string.passingDate, item.time.toString("dd.MM.YYY HH:mm"))

        if (item.test.isFavourite) favouriteImage.setImageResource(R.drawable.ic_favourite_black_24dp)
        else favouriteImage.setImageResource(R.drawable.ic_not_favourite_black_24dp)

        resultText.maxLines = if (item.fullText) 1024 else 5

        resultShowMore.setOnClickListener {
            notifyItemChanged(holder.adapterPosition)
            item.fullText = !item.fullText
        }
    }

    override fun getLayoutId(): Int = R.layout.item_fact_test_result

    override fun isForViewType(items: List<*>, position: Int): Boolean =
        items[position] is FactResult
}