package ru.lingstra.communications.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class AppPrefsStorage @Inject constructor(private val context: Context) : AppPrefs {

    companion object {
        private const val USER_ID_KEY = "userId"
        private const val ONLY_FAVOURITES_KEY = "ONLY_FAVOURITES_KEY"
    }

    private val sharedPrefs: SharedPreferences
            by lazy { context.getSharedPreferences("appPrefs", 0) }
//TODO login!!!
    override var userId: String
        get() = sharedPrefs.getString(USER_ID_KEY, "") ?: ""
        set(value) = sharedPrefs.edit { putString(USER_ID_KEY, value) }

    override var onlyFavourites: Boolean
        get() = sharedPrefs.getBoolean(ONLY_FAVOURITES_KEY, false)
        set(value) = sharedPrefs.edit { putBoolean(ONLY_FAVOURITES_KEY, value) }
}