package ru.lingstra.communications.presentation.list

import ru.lingstra.communications.domain.hand_shakes.TestsListInteractor
import ru.lingstra.communications.domain.hand_shakes.TestsListViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.ResourceManager
import ru.lingstra.communications.system.SystemMessage
import javax.inject.Inject

class TestsListPresenter @Inject constructor(
    private val resourceManager: ResourceManager,
    private val systemMessage: SystemMessage,
    private val interactor: TestsListInteractor
) : BaseMviPresenter<TestsListView, TestsListViewState>() {

    override fun bindIntents() {

    }
}