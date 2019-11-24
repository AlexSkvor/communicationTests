package ru.lingstra.communications.ui.test_passing

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
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
import ru.lingstra.communications.ui.utils.ItemDecorationBottom
import ru.lingstra.communications.ui.utils.delegate.CompositeDelegateAdapter
import ru.lingstra.communications.ui.utils.delegate.pressedItems
import ru.lingstra.communications.visible

const val ARG_TAG_TEST_PASSING_FRAGMENT: String = "TestPassingFragment"

class TestPassingFragment : MviBaseFragment<TestPassingView, TestPassingPresenter>(),
    TestPassingView {

    override fun startIntent(): Observable<Unit> = playButton.clicks()

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

    private fun startedRender(state: TestPassingViewState) {

        if (state.result == null) {
            completedGroup.visible = false
            testStartedGroup.visible = true
            testDescriptionGroup.visible = false
            if (questionsAdapter.itemCount != state.test.questions.size) {
                questionsAdapter.replaceData(state.test.questions)
            }
            completeButton.visible = (state.test.questions.size == state.answers.size)
        } else {
            completedGroup.visible = true
            testStartedGroup.visible = false
            testDescriptionGroup.visible = false
            backButton.setOnClickListener { requireActivity().onBackPressed() }
            result.text = state.result.text
        }
    }

    private fun notStartedRender(state: TestPassingViewState) {
        completedGroup.visible = false
        testStartedGroup.visible = false
        testDescriptionGroup.visible = true

        requireActivity().title = state.test.name
        description.text = state.test.description
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        requireNotNull(inflater)
        questionsAdapter = CompositeDelegateAdapter.Companion.Builder<Test.Question>()
            .add(QuestionAdapter(inflater))
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result.movementMethod = ScrollingMovementMethod()
        description.movementMethod = ScrollingMovementMethod()
        (requireActivity() as AppActivity).bottomNavigationVisibility = false
        setHasOptionsMenu(true)
        questionsRecycler.layoutManager = LinearLayoutManager(requireContext())
        val space = resources.getDimensionPixelSize(R.dimen.marginNormal)
        questionsRecycler.addItemDecoration(ItemDecorationBottom(space))
        questionsRecycler.adapter = questionsAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as AppActivity).bottomNavigationVisibility = true
        requireActivity().title = getString(R.string.app_name)
    }
}