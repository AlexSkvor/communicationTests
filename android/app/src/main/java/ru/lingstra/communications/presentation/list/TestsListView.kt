package ru.lingstra.communications.presentation.list

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.test_list.TestsListViewState

interface TestsListView : MvpView {
    fun loadListIntent(): Observable<Unit>
    fun syncIntent(): Observable<Unit>
    fun testClicked(): Observable<Test>

    fun render(state: TestsListViewState)
}