package ru.lingstra.communications.presentation.list

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.communications.domain.test_list.TestsListViewState

interface TestsListView : MvpView {
    fun action(): Observable<Unit>

    fun render(state: TestsListViewState)
}