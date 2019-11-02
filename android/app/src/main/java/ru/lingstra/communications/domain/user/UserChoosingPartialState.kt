package ru.lingstra.communications.domain.user

import ru.lingstra.communications.data.database.entities.UserEntity

sealed class UserChoosingPartialState(private val logMessage: String) {

    data class Loading(val loading: Boolean) : UserChoosingPartialState("Loading $loading")
    data class Error(val t: Throwable) : UserChoosingPartialState("Error $t")
    data class Users(val users: List<UserEntity>) : UserChoosingPartialState("Users: $users")

    fun partial() = this
    override fun toString(): String = logMessage
}