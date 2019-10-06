package ru.lingstra.avitocopy.presentation.token

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface TokenView: MvpView {
    fun newToken(): Observable<String>
}