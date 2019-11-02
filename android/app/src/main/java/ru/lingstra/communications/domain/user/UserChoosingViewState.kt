package ru.lingstra.communications.domain.user

import ru.lingstra.communications.data.database.entities.UserEntity

data class UserChoosingViewState(
    val users: List<UserEntity> = emptyList()
)