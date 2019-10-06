package ru.lingstra.communications.presentation.token

import ru.lingstra.communications.data.prefs.AppPrefs
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.SystemMessage
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