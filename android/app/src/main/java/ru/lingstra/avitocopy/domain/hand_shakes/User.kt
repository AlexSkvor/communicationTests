package ru.lingstra.avitocopy.domain.hand_shakes

import ru.lingstra.avitocopy.data.repository.SimplestUser

data class User(
    val name: String,
    val surname: String,
    val photo: String,
    val id: String
) {
    constructor(s: SimplestUser) : this(
        name = s.item.first_name,
        surname = s.item.last_name,
        photo = s.item.photo_100,
        id = s.item.id.toString()
    )

    val link = "https://vk.com/id$id"
}