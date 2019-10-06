package ru.lingstra.communications.presentation.list

import com.hannesdorfmann.mosby3.mvp.MvpView
import ru.lingstra.communications.domain.hand_shakes.TestsListViewState

interface TestsListView: MvpView {
    fun render(state: TestsListViewState)
}