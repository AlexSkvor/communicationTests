package ru.lingstra.communications.data.prefs

import io.reactivex.Observable
import ru.lingstra.communications.data.database.entities.UserEntity

interface AppPrefs {
    var user: UserEntity
    var onlyFavourites: Boolean
    fun userChanges(): Observable<UserEntity>
    fun onlyFavouritesFlagChanges(): Observable<Boolean>
}