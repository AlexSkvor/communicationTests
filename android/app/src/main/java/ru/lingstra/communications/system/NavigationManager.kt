package ru.lingstra.communications.system

import androidx.annotation.IdRes
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class NavigationManager {
    private val notifierRelay = PublishRelay.create<NavigationAction>()
    val actions: Observable<NavigationAction> = notifierRelay.hide()

    fun acceptAction(action: NavigationAction) = notifierRelay.accept(action)

    fun navigate(screenId: Int) = acceptAction(NavigationAction.Screen(screenId))
    fun back() = acceptAction(NavigationAction.Back)

    sealed class NavigationAction {
        data class Screen(
            @IdRes val screenId: Int
        ) : NavigationAction()

        object Back : NavigationAction()
    }
}