package ru.lingstra.communications.ui.token

import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_token.*
import ru.lingstra.communications.R
import ru.lingstra.communications.presentation.token.TokenPresenter
import ru.lingstra.communications.presentation.token.TokenView
import ru.lingstra.communications.ui.base.MviBaseFragment

class TokenFragment : MviBaseFragment<TokenView, TokenPresenter>(), TokenView {
    override fun createPresenter(): TokenPresenter =
        scope.getInstance(TokenPresenter::class.java)

    override val layoutRes: Int
        get() = R.layout.fragment_token

    override fun newToken(): Observable<String> =
        saveTokenButton.clicks()
            .map { newTokenEdit.text.toString() }
            .filter { it.isNotBlank() }
}