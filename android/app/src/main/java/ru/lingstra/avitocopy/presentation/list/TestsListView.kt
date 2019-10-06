package ru.lingstra.avitocopy.presentation.list

import com.hannesdorfmann.mosby3.mvp.MvpView
import ru.lingstra.avitocopy.domain.hand_shakes.TestsListViewState

interface TestsListView: MvpView {
    fun render(state: TestsListViewState)
}