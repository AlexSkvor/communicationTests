package ru.lingstra.avitocopy.ui.list

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_hand_shakes.*
import ru.lingstra.avitocopy.R
import ru.lingstra.avitocopy.domain.hand_shakes.HandShakesViewState
import ru.lingstra.avitocopy.domain.hand_shakes.User
import ru.lingstra.avitocopy.presentation.list.HandShakesPresenter
import ru.lingstra.avitocopy.presentation.list.HandShakesView
import ru.lingstra.avitocopy.ui.base.MviBaseFragment
import ru.lingstra.avitocopy.ui.utils.delegate.CompositeDelegateAdapter
import ru.lingstra.avitocopy.ui.utils.delegate.UserAction
import ru.lingstra.avitocopy.visible
import toothpick.Scope
import toothpick.config.Module
import android.content.Intent
import android.net.Uri

class HandShakesFragment : MviBaseFragment<HandShakesView, HandShakesPresenter>(), HandShakesView {

    override val layoutRes: Int
        get() = R.layout.fragment_hand_shakes

    override fun createPresenter(): HandShakesPresenter =
        scope.getInstance(HandShakesPresenter::class.java)

    private lateinit var adapter: CompositeDelegateAdapter<User>

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                //bind(NetworkProvider::class.java).to(NetworkSimple::class.java).singletonInScope()
            }
        })
    }

    override fun render(state: HandShakesViewState) {
        adapter.replaceData(state.answerList)
        renderTimeAndSearch(state)
    }

    private fun onUserClicked() =
        adapter.actions.ofType(UserAction.ItemPressed::class.java)
            .map { it.item }.ofType(User::class.java)

    private fun renderTimeAndSearch(state: HandShakesViewState) {
        timeQueriesLayout.visible = state.loaded || state.loadingInProcess
        if (state.loaded || state.loadingInProcess) {
            val time = state.endTime - state.startTime
            timeField.text = getString(R.string.timeSinceStart, time.toInt())
            queriesField.text = getString(R.string.queriesSinceStart, state.queries)
        }
    }

    override fun startSearch(): Observable<Pair<String, String>> =
        start.clicks()
            .map { firstRefEdit.text.toString() to secondRefEdit.text.toString() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = CompositeDelegateAdapter.Companion.Builder<User>()
            .add(UserListAdapter())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        onUserClicked().subscribe {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
            startActivity(intent)
        }.bind()
    }

    private fun setupRecycler() {
        answerRecycler.layoutManager = LinearLayoutManager(requireContext())
        answerRecycler.adapter = adapter
    }
}