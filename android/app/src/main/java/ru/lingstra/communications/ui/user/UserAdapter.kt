package ru.lingstra.communications.ui.user

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_user.view.*
import ru.lingstra.communications.R
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.ui.utils.delegate.DelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.UserAction
import ru.lingstra.communications.visible

class UserAdapter(private val selectedId: () -> String) : DelegateAdapter<UserEntity>() {

    private val actionsRelay = PublishRelay.create<UserAction<UserEntity>>()
    override fun getAction(): Observable<UserAction<UserEntity>> = actionsRelay.hide()

    override fun onBind(item: UserEntity, holder: DelegateViewHolder) = with(holder.itemView) {
        deletePointImage.visible = item.id != selectedId()
        radioButtonUser.isChecked = item.id == selectedId()
        radioButtonUser.isClickable = false
        userName.text = item.name
        setOnClickListener {
            actionsRelay.accept(UserAction.ItemPressed(item))
        }
        deletePointImage.setOnClickListener {
            actionsRelay.accept(UserAction.ItemDeleted(item))
        }
    }

    override fun getLayoutId(): Int = R.layout.item_user

    override fun isForViewType(items: List<*>, position: Int): Boolean =
        items[position] is UserEntity
}