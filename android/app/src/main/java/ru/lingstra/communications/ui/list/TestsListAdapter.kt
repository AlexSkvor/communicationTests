package ru.lingstra.communications.ui.list

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_test.view.*
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.ui.utils.delegate.DelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.UserAction

class TestsListAdapter : DelegateAdapter<Test>() {

    private val actionsRelay = PublishRelay.create<UserAction<Test>>()
    override fun getAction(): Observable<UserAction<Test>> = actionsRelay.hide()

    override fun onBind(item: Test, holder: DelegateViewHolder) =
        with(holder.itemView) {
            testName.text = resources.getString(R.string.testName, item.name)
            questionsCount.text = resources.getString(R.string.questionsNumber, item.questions.size)
            if (item.isFavourite) favouriteImage.setImageResource(R.drawable.ic_favourite_black_24dp)
            else favouriteImage.setImageResource(R.drawable.ic_not_favourite_black_24dp)

            setOnClickListener { actionsRelay.accept(UserAction.ItemPressed(item)) }
            favouriteImage.setOnClickListener { actionsRelay.accept(UserAction.ItemEdit(item)) }
        }

    override fun getLayoutId(): Int = R.layout.item_test

    override fun isForViewType(items: List<*>, position: Int): Boolean = items[position] is Test
}