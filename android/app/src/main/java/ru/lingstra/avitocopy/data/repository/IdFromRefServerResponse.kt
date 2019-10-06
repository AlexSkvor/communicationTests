package ru.lingstra.avitocopy.data.repository

import ru.lingstra.avitocopy.domain.hand_shakes.FriendItem

data class IdFromRefServerResponse(
    val response: List<FriendItem>
)