package ru.lingstra.communications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

fun Fragment.hideKeyboard() {
    val inputManager = requireActivity()
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

var View.visible: Boolean
    set(value) {
        this.visibility = if (value) View.VISIBLE
        else View.GONE
    }
    get() = this.visibility == View.VISIBLE

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun String.firstUpper(): String {
    if (this.isEmpty()) return this
    val begin = this.first()
    val end = this.substring(1, this.length)
    return begin.toUpperCase() + end
}

inline fun <reified T> Observable<T>.endWith(tail: T): Observable<T> =
    this.concatWith(Observable.just(tail))

inline fun <reified T> T.alsoPrintDebug(message: String): T =
    this.also { Timber.e("$message...$it") }

fun BottomNavigationView.setupWithNavControllerReselectionDisabled(navController: NavController) {
    this.setupWithNavController(navController)
    this.setOnNavigationItemReselectedListener { }
}

fun String.toIntOrMinusOne(): Int = try {
    this.toInt()
} catch (e: Exception) {
    0
}

inline fun <reified T, N> Single<List<T>>.flattenMap(crossinline mapper: (T) -> N): Single<List<N>> =
    flattenAsFlowable { it }.map { mapper(it) }.toList()

fun BottomNavigationView.setupLongText() {
    val menuView = this.getChildAt(0) as? ViewGroup ?: return
    menuView.forEach {
        it.findViewById<View>(R.id.largeLabel)?.setPadding(0, 0, 0, 40)
    }
}

inline fun <reified T : Any> Bundle.withArgument(key: String, value: T?): Bundle {
    return if (value == null) this
    else when (value) {
        is String -> this.also { putString(key, value) }
        is Int -> this.also { putInt(key, value) }
        is Long -> this.also { putLong(key, value) }
        is Boolean -> this.also { putBoolean(key, value) }
        else -> throw IllegalArgumentException("Wrong value typeChosen! ${value::class}")
    }
}