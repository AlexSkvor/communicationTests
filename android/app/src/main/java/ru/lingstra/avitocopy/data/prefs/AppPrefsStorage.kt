package ru.lingstra.avitocopy.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class AppPrefsStorage @Inject constructor(private val context: Context) : AppPrefs {

    companion object {
        private const val TOKEN_KEY = "token for vk api"
    }

    private val sharedPrefs: SharedPreferences
            by lazy { context.getSharedPreferences("appPrefs", 0) }

    override var token: String
        get() = sharedPrefs.getString(TOKEN_KEY, "") ?: ""
        set(value) = sharedPrefs.edit { putString(TOKEN_KEY, value) }
}