package ru.lingstra.communications.presentation.base

import javax.inject.Inject

class StubPresenter @Inject constructor(): BaseMviPresenter<StubView, Any>() {
    override fun bindIntents() {}
}