package ru.lingstra.communications.system

import androidx.annotation.IdRes
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.lingstra.communications.R

class NavigationManager {

    private var currentDestId: Int? = null

    val arguments: MutableMap<String, Any> = mutableMapOf()

    private val notifierRelay: PublishRelay<NavigationAction> =
        PublishRelay.create<NavigationAction>()
    val actions: Observable<NavigationAction> = notifierRelay.hide()

    private fun acceptAction(action: NavigationAction) = notifierRelay.accept(action)

    fun navigate(screenId: Int) {
        if (currentDestId != screenId) {
            currentDestId = screenId
            acceptAction(NavigationAction.Screen(screenId))
        }
    }

    fun back() = acceptAction(NavigationAction.Back).also { currentDestId = null }

    private val syncRelay: PublishRelay<Unit> = PublishRelay.create<Unit>()
    fun syncPlease() = syncRelay.accept(Unit)
    val syncActions: Observable<Unit> = syncRelay.hide()

    sealed class NavigationAction {
        data class Screen(
            @IdRes val screenId: Int
        ) : NavigationAction()

        object Back : NavigationAction()
    }
}