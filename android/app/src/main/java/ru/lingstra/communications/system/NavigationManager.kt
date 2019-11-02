package ru.lingstra.communications.system

import androidx.annotation.IdRes
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.lingstra.communications.R

class NavigationManager {

    val arguments: MutableMap<String, Any> = mutableMapOf()

    private val notifierRelay = PublishRelay.create<NavigationAction>()
    val actions: Observable<NavigationAction> = notifierRelay.hide()

    private fun acceptAction(action: NavigationAction) = notifierRelay.accept(action)

    fun navigate(screenId: Int) = acceptAction(NavigationAction.Screen(screenId))
    fun back() = acceptAction(NavigationAction.Back)
    fun relogin() = acceptAction(NavigationAction.Screen(R.id.userChoosingFragment))

    sealed class NavigationAction {
        data class Screen(
            @IdRes val screenId: Int
        ) : NavigationAction()

        object Back : NavigationAction()
    }
}