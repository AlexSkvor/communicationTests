package ru.lingstra.avitocopy.presentation.token

import ru.lingstra.avitocopy.data.prefs.AppPrefs
import ru.lingstra.avitocopy.presentation.base.BaseMviPresenter
import ru.lingstra.avitocopy.system.SystemMessage
import javax.inject.Inject

class TokenPresenter @Inject constructor(
    private val prefs: AppPrefs,
    private val systemMessage: SystemMessage
) : BaseMviPresenter<TokenView, Any>() {
    override fun bindIntents() {
        intent(TokenView::newToken)
            .subscribe {
               prefs.token = it
                systemMessage.send(prefs.token)
            }.bind()
    }
}