package ru.lingstra.communications.data.database.providers

import android.content.Context
import androidx.room.Room
import ru.lingstra.communications.data.database.AppDatabase
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseProvider @Inject constructor(private val context: Context): Provider<AppDatabase> {
    override fun get(): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "database").build()
}