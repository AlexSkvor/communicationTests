package ru.lingstra.communications.ui.user

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_dialog_new_user.view.*
import ru.lingstra.communications.R
import ru.lingstra.communications.data.repository.UserChoosingRepos
import ru.lingstra.communications.presentation.base.StubPresenter
import ru.lingstra.communications.presentation.base.StubView
import ru.lingstra.communications.toothpick.DI
import ru.lingstra.communications.ui.base.MviBaseDialogFragment
import ru.lingstra.communications.withArgument

class NewUserFragment : MviBaseDialogFragment<StubView, StubPresenter>(), StubView {
    override val layoutRes: Int
        get() = R.layout.fragment_dialog_new_user

    override fun createPresenter(): StubPresenter = fromScope()

    lateinit var inflatedView: View

    companion object {
        fun newInstance(parentScope: String? = null): NewUserFragment {
            return NewUserFragment().apply {
                arguments = Bundle().withArgument(PARENT_SCOPE_NAME, parentScope ?: DI.APP_SCOPE)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        inflatedView = requireActivity().layoutInflater.inflate(layoutRes, null)

        return AlertDialog.Builder(requireContext(), R.style.DialogTheme)
            .setView(inflatedView)
            .setCancelable(false)
            .create()
    }

    private val repository: UserChoosingRepos by lazy { fromScope<UserChoosingRepos>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflatedView.cancelUserCreation.setOnClickListener { dismiss() }
        inflatedView.addUser.setOnClickListener {
            val name = inflatedView.newUserNameEdit.text?.toString().orEmpty()
            if (!name.isBlank()) {
                repository.addUser(name)
                    .subscribe {
                        repository.userAdditions.accept(Unit)
                        dismiss()
                    }.bind()
            }
        }
    }
}