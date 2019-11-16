package ru.lingstra.communications.presentation.user

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import ru.lingstra.communications.R
import ru.lingstra.communications.domain.user.UserChoosingInteractor
import ru.lingstra.communications.domain.user.UserChoosingPartialState
import ru.lingstra.communications.domain.user.UserChoosingViewState
import ru.lingstra.communications.presentation.base.BaseMviPresenter
import ru.lingstra.communications.system.ResourceManager
import ru.lingstra.communications.system.SystemMessage
import timber.log.Timber
import javax.inject.Inject

class UserChoosingPresenter @Inject constructor(
    private val systemMessage: SystemMessage,
    private val resourceManager: ResourceManager,
    private val interactor: UserChoosingInteractor
) : BaseMviPresenter<UserChoosingView, UserChoosingViewState>() {

    override fun bindIntents() {
        val actions = getActions().share()
        subscribeActions(actions)
        subscribeViewState(
            actions.scan(UserChoosingViewState(), reducer).distinctUntilChanged(),
            UserChoosingView::render
        )
    }

    private val reducer =
        BiFunction { oldState: UserChoosingViewState, it: UserChoosingPartialState ->
            when (it) {
                is UserChoosingPartialState.Users -> oldState.copy(users = it.users)
                is UserChoosingPartialState.Loading -> oldState
                is UserChoosingPartialState.Error -> oldState
            }
        }

    private fun subscribeActions(action: Observable<UserChoosingPartialState>) {
        action.subscribe {
            when (it) {
                is UserChoosingPartialState.Error -> {
                    systemMessage.send(resourceManager.getString(R.string.errorHappened))
                    Timber.e(it.t)
                }
                is UserChoosingPartialState.Loading -> systemMessage.showProgress(it.loading)
            }
        }.bind()
    }

    private fun getActions(): Observable<UserChoosingPartialState> {

        val loadAction = intent(UserChoosingView::init)
            .switchMap { interactor.getUsers() }

        val userAdditionsAction = interactor.userAdditions()

        val deleteAction = intent(UserChoosingView::deleteUser)
            .switchMap { interactor.deleteUserAndReloadList(it) }

        val list = listOf(loadAction, deleteAction, userAdditionsAction)
        return Observable.merge(list)
    }
}