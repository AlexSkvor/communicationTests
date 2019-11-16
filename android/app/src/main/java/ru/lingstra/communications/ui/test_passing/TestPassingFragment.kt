package ru.lingstra.communications.ui.test_passing

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_test_passing.*
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.models.Test
import ru.lingstra.communications.domain.test_passing.TestPassingViewState
import ru.lingstra.communications.presentation.test_passing.TestPassingPresenter
import ru.lingstra.communications.presentation.test_passing.TestPassingView
import ru.lingstra.communications.ui.AppActivity
import ru.lingstra.communications.ui.base.MviBaseFragment
import ru.lingstra.communications.ui.utils.delegate.CompositeDelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.pressedItems
import ru.lingstra.communications.visible

const val ARG_TAG_TEST_PASSING_FRAGMENT: String = "TestPassingFragment"

class TestPassingFragment : MviBaseFragment<TestPassingView, TestPassingPresenter>(),
    TestPassingView {

    private lateinit var questionsAdapter: CompositeDelegateAdapter<Test.Question>

    override val layoutRes: Int
        get() = R.layout.fragment_test_passing

    override fun createPresenter(): TestPassingPresenter =
        scope.getInstance(TestPassingPresenter::class.java)

    override fun answerChosenIntent(): Observable<Test.Question> =
        questionsAdapter.actions.pressedItems()

    override fun completeIntent(): Observable<Unit> = completeButton.clicks()

    override fun render(state: TestPassingViewState) {
        if (state.started) startedRender(state)
        else notStartedRender(state)
    }

    private fun startedRender(state: TestPassingViewState){
        if (state.result == null) {
            titleTest.text = state.test.name
            description.text = state.test.description
            if (questionsAdapter.itemCount != state.test.questions.size) {
                questionsAdapter.replaceData(state.test.questions)
            }
            completeButton.visible = (state.test.questions.size == state.answers.size)
        } else {
            questionsRecycler.visible = false
            completeButton.visible = false
            backButton.visible = true
            backButton.setOnClickListener { requireActivity().onBackPressed() }
            result.visible = true
            result.text = state.result.text
        }
    }

    private fun notStartedRender(state: TestPassingViewState){
        requireActivity().title = state.test.name
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        questionsAdapter = CompositeDelegateAdapter.Companion.Builder<Test.Question>()
            .add(QuestionAdapter())
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppActivity).bottomNavigationVisibility = false
        setHasOptionsMenu(true)
        questionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        questionsRecycler.adapter = questionsAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as AppActivity).bottomNavigationVisibility = true
        requireActivity().title = getString(R.string.app_name)
    }
}