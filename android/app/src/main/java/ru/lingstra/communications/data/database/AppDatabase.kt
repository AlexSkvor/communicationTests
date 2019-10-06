package ru.lingstra.communications.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.lingstra.communications.data.database.dao.UserDao
import ru.lingstra.communications.data.database.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}