package ru.lingstra.communications.data.prefs

import ru.lingstra.communications.data.database.entities.UserEntity

interface AppPrefs {
    var user: UserEntity
    var onlyFavourites: Boolean
}