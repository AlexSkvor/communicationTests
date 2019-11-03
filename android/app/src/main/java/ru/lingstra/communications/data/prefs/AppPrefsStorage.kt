package ru.lingstra.communications.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.lingstra.communications.data.database.entities.UserEntity
import javax.inject.Inject

class AppPrefsStorage @Inject constructor(private val context: Context) : AppPrefs {

    companion object {
        private const val USER_ID_KEY = "userId"
        private const val USER_NAME_KEY = "userName"
        private const val ONLY_FAVOURITES_KEY = "ONLY_FAVOURITES_KEY"
    }

    private val userRelay: PublishRelay<UserEntity> = PublishRelay.create()
    override fun userChanges(): Observable<UserEntity> =
        userRelay.hide().observeOn(AndroidSchedulers.mainThread())

    private val favouritesRelay: PublishRelay<Boolean> = PublishRelay.create()
    override fun onlyFavouritesFlagChanges(): Observable<Boolean> =
        favouritesRelay.hide().observeOn(AndroidSchedulers.mainThread())

    private val sharedPrefs: SharedPreferences
            by lazy { context.getSharedPreferences("appPrefs", 0) }

    override var onlyFavourites: Boolean
        get() = sharedPrefs.getBoolean(ONLY_FAVOURITES_KEY, false)
        set(value) {
            sharedPrefs.edit { putBoolean(ONLY_FAVOURITES_KEY, value) }
            favouritesRelay.accept(value)
        }
    override var user: UserEntity
        get() {
            val id = sharedPrefs.getString(USER_ID_KEY, "") ?: ""
            val name = sharedPrefs.getString(USER_NAME_KEY, "") ?: ""
            return UserEntity(id, name)
        }
        set(value) {
            sharedPrefs.edit { putString(USER_ID_KEY, value.id) }
            sharedPrefs.edit { putString(USER_NAME_KEY, value.name) }
            userRelay.accept(value)
        }

}