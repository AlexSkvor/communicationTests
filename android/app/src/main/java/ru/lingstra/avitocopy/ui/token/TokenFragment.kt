package ru.lingstra.avitocopy.ui.token

import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_token.*
import ru.lingstra.avitocopy.R
import ru.lingstra.avitocopy.presentation.token.TokenPresenter
import ru.lingstra.avitocopy.presentation.token.TokenView
import ru.lingstra.avitocopy.ui.base.MviBaseFragment

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