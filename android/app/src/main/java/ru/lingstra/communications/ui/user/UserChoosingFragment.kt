package ru.lingstra.communications.ui.user

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_user_choosing.*
import ru.lingstra.communications.R
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.domain.user.UserChoosingViewState
import ru.lingstra.communications.presentation.user.UserChoosingPresenter
import ru.lingstra.communications.presentation.user.UserChoosingView
import ru.lingstra.communications.ui.base.MviBaseFragment
import ru.lingstra.communications.ui.utils.delegate.CompositeDelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.deletedItems
import ru.lingstra.communications.ui.utils.delegate.pressedItems
import javax.inject.Inject

class UserChoosingFragment : MviBaseFragment<UserChoosingView, UserChoosingPresenter>(),
    UserChoosingView {

    @Inject
    lateinit var appPrefs: AppPrefs

    override val layoutRes: Int = R.layout.fragment_user_choosing

    override fun createPresenter(): UserChoosingPresenter =
        scope.getInstance(UserChoosingPresenter::class.java)

    private lateinit var usersAdapter: CompositeDelegateAdapter<UserEntity>

    override fun newUser(): Observable<String> = addUserButton.clicks()
        .filter { !newUserNameEdit.text.isNullOrBlank() }
        .map { newUserNameEdit.text.toString() }

    override fun deleteUser(): Observable<UserEntity> = usersAdapter.actions.deletedItems()

    override fun render(state: UserChoosingViewState) {
        usersAdapter.replaceData(state.users)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        usersAdapter = CompositeDelegateAdapter.Companion.Builder<UserEntity>()
            .add(UserAdapter { appPrefs.user.id })
            .build()

        usersAdapter.actions.pressedItems()
            .subscribe {
                appPrefs.user = it
                usersAdapter.notifyDataSetChanged()
            }.bind()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
    }

    private fun setupAdapters() {
        usersRecycler.layoutManager = LinearLayoutManager(requireContext())
        usersRecycler.adapter = usersAdapter
    }
}