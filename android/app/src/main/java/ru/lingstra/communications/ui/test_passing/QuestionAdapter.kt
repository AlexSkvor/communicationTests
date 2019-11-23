package ru.lingstra.communications.ui.test_passing

import android.widget.RadioButton
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_question.view.*
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.ui.utils.delegate.DelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.UserAction
import android.view.LayoutInflater
import ru.lingstra.communications.R

class QuestionAdapter(
    private val inflater: LayoutInflater
) : DelegateAdapter<Test.Question>() {

    private val relay = PublishRelay.create<UserAction<Test.Question>>()
    override fun getAction(): Observable<UserAction<Test.Question>> = relay.hide()

    override fun onBind(item: Test.Question, holder: DelegateViewHolder) =
        with(holder.itemView) {
            questionText.text = "\t" + item.text
            radioGroup.removeAllViews()
            item.answers.forEach {
                val button = inflater.inflate(R.layout.radio_button, null) as RadioButton//RadioButton(context)
                button.text = it.text
                button.textSize = 16f
                if (it.chosen) button.isChecked = true
                radioGroup.addView(button)
                button.setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        item.answers.forEach { ans -> ans.chosen = false }
                        it.chosen = true
                        val message = item.copy(answers = listOf(it))
                        relay.accept(UserAction.ItemPressed(message))
                    }
                }
            }
        }

    override fun getLayoutId(): Int = R.layout.item_question

    override fun isForViewType(items: List<*>, position: Int): Boolean =
        items[position] is Test.Question
}