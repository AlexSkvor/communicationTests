package ru.lingstra.avitocopy.domain.hand_shakes

data class FriendsServerAnswer(
    val response: Response?
)

data class Response(
    val count: Int,
    val items: List<FriendItem>
)

data class FriendItem(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val is_closed: Boolean,
    val can_access_closed: Boolean,
    val photo_100: String
)