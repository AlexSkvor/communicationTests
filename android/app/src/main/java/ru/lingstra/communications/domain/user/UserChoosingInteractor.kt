package ru.lingstra.communications.domain.user

import io.reactivex.Observable
import ru.lingstra.communications.data.database.entities.UserEntity
import ru.lingstra.communications.data.repository.UserChoosingRepository
import ru.lingstra.communications.endWith
import javax.inject.Inject

class UserChoosingInteractor @Inject constructor(
    private val repository: UserChoosingRepository
) {

    fun getUsers(): Observable<UserChoosingPartialState> = getUsersList()
        .startWith(UserChoosingPartialState.Loading(true))
        .onErrorReturn { UserChoosingPartialState.Error(it) }
        .endWith(UserChoosingPartialState.Loading(false))

    fun addNewUserAndReloadList(newUser: String): Observable<UserChoosingPartialState> =
        repository.addUser(newUser)
            .andThen(getUsersList())
            .startWith(UserChoosingPartialState.Loading(true))
            .onErrorReturn { UserChoosingPartialState.Error(it) }
            .endWith(UserChoosingPartialState.Loading(false))

    fun deleteUserAndReloadList(user: UserEntity): Observable<UserChoosingPartialState> =
        repository.deleteUser(user)
            .andThen(getUsersList())
            .startWith(UserChoosingPartialState.Loading(true))
            .onErrorReturn { UserChoosingPartialState.Error(it) }
            .endWith(UserChoosingPartialState.Loading(false))

    private fun getUsersList() = repository.loadUsers()
        .map { UserChoosingPartialState.Users(it).partial() }

}