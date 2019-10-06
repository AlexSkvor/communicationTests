package ru.lingstra.avitocopy.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.hannesdorfmann.mosby3.mvi.MviPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import ru.lingstra.avitocopy.objectScopeName
import ru.lingstra.avitocopy.system.disposables.AppDisposables
import ru.lingstra.avitocopy.system.disposables.DisposablesProvider
import ru.lingstra.avitocopy.toothpick.DI
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick

abstract class MviBaseFragment <V: MvpView,P : MviPresenter<V, *>> : MviFragment<V, P>(), DisposablesProvider by AppDisposables() {
    abstract val layoutRes: Int
        @LayoutRes get

    private companion object{
        const val STATE_SCOPE_NAME = "state_scope_name"
    }

    protected val parentScopeName: String by lazy {
        (parentFragment?.parentFragment as? MviBaseFragment<*,*>)?.fragmentScopeName ?: DI.APP_SCOPE
    }

    private lateinit var fragmentScopeName: String

    protected lateinit var scope: Scope
        private set

    protected open fun installModules(scope: Scope) {}

    private val TAG: String
        get() = javaClass.simpleName

    private var mLifeCycleLogsEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        if (Toothpick.isScopeOpen(fragmentScopeName)) {
            Timber.d("Get exist UI scope: $fragmentScopeName")
            scope = Toothpick.openScope(fragmentScopeName)
        } else {
            Timber.d("Init new UI scope: $fragmentScopeName")
            scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            installModules(scope)
        }
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            log("onCreate(): fragment re-created from savedInstanceState")
        } else {
            log("onCreate(): fragment created anew")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toothpick.inject(this, scope)
        log("onViewCreated()")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        log("onViewStateRestored()")
    }

    override fun onStart() {
        super.onStart()
        log("onStart()")
    }

    override fun onResume() {
        super.onResume()
        log("onResume()")
    }

    override fun onPause() {
        super.onPause()
        log("onPause()")
    }

    override fun onStop() {
        super.onStop()
        log("onStop()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView()")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onDestroy() {
        Toothpick.closeScope(scope.name)
        clear()
        super.onDestroy()
        log("onDestroy()")
    }

    private fun log(log: String) {
        if (mLifeCycleLogsEnabled) {
            Timber.d("$TAG - $log")
        }
    }
}