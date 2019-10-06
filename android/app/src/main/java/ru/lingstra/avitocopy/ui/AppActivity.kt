package ru.lingstra.avitocopy.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_app.*
import org.jetbrains.anko.toast
import ru.lingstra.avitocopy.R
import ru.lingstra.avitocopy.alsoPrintDebug
import ru.lingstra.avitocopy.domain.app.AppViewState
import ru.lingstra.avitocopy.presentation.app.AppPresenter
import ru.lingstra.avitocopy.presentation.app.AppView
import ru.lingstra.avitocopy.setupWithNavControllerReselectionDisabled
import ru.lingstra.avitocopy.toothpick.DI
import ru.lingstra.avitocopy.ui.base.ProgressDialogFragment
import toothpick.Toothpick

class AppActivity : MviActivity<AppView, AppPresenter>(), AppView {

    private val scope = Toothpick.openScope(DI.APP_SCOPE)
    override fun createPresenter(): AppPresenter = scope.getInstance(AppPresenter::class.java)

    companion object {
        private const val DIALOG_TAG = "dialog fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, scope)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }

    private val refreshIntent: PublishRelay<Unit> = PublishRelay.create()
    override fun refresh(): Observable<Unit> = refreshIntent.hide()

    private val navigator: NavController by lazy { findNavController(R.id.mainNavHostFragment) }

    override fun render(state: AppViewState) {
        when (state.render) {
            AppViewState.Render.TOAST -> showToast(state.toast)
            AppViewState.Render.PROGRESS -> showProgress(state.progress)
            else -> {
            }
        }
        if (state.render != AppViewState.Render.NONE) refreshIntent.accept(Unit)
    }

    override fun onResume() {
        super.onResume()
        bottomNavigation.setupWithNavControllerReselectionDisabled(navigator)
    }

    private fun showProgress(visible: Boolean) {
        val fragment = supportFragmentManager.findFragmentByTag(DIALOG_TAG)
        if (fragment != null && !visible) {
            (fragment as ProgressDialogFragment).dismissAllowingStateLoss()
            supportFragmentManager.executePendingTransactions()
        } else if (fragment == null && visible) {
            val progressDialogFragment = ProgressDialogFragment()
            progressDialogFragment.show(supportFragmentManager, DIALOG_TAG)
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun showToast(message: String) {
        if (message.isNotBlank()) toast(message)
    }
}