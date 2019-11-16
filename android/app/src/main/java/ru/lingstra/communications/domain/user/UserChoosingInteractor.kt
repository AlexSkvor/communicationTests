package ru.lingstra.communications.domain.user

import io.reactivex.Observable
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.data.repository.UserChoosingRepos
import ru.lingstra.communications.endWith
import javax.inject.Inject

class UserChoosingInteractor @Inject constructor(
    private val repository: UserChoosingRepos
) {

    fun getUsers(): Observable<UserChoosingPartialState> = getUsersList()
        .startWith(UserChoosingPartialState.Loading(true))
        .onErrorReturn { UserChoosingPartialState.Error(it) }
        .endWith(UserChoosingPartialState.Loading(false))

    fun userAdditions(): Observable<UserChoosingPartialState> = repository.userAdditions.hide()
        .switchMap { getUsersList() }
        .onErrorReturn { UserChoosingPartialState.Error(it) }

    fun deleteUserAndReloadList(user: UserEntity): Observable<UserChoosingPartialState> =
        repository.deleteUser(user)
            .andThen(getUsersList())
            .startWith(UserChoosingPartialState.Loading(true))
            .onErrorReturn { UserChoosingPartialState.Error(it) }
            .endWith(UserChoosingPartialState.Loading(false))

    private fun getUsersList() = repository.loadUsers()
        .map { UserChoosingPartialState.Users(it).partial() }

}