package ru.lingstra.avitocopy.presentation.list

import ru.lingstra.avitocopy.domain.hand_shakes.TestsListInteractor
import ru.lingstra.avitocopy.domain.hand_shakes.TestsListViewState
import ru.lingstra.avitocopy.presentation.base.BaseMviPresenter
import ru.lingstra.avitocopy.system.ResourceManager
import ru.lingstra.avitocopy.system.SystemMessage
import javax.inject.Inject

class TestsListPresenter @Inject constructor(
    private val resourceManager: ResourceManager,
    private val systemMessage: SystemMessage,
    private val interactor: TestsListInteractor
) : BaseMviPresenter<TestsListView, TestsListViewState>() {

    override fun bindIntents() {

    }
}