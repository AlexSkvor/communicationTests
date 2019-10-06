package ru.lingstra.communications.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.lingstra.communications.data.database.dao.QuestionDao
import ru.lingstra.communications.data.database.dao.ResultReferenceDao
import ru.lingstra.communications.data.database.dao.TestDao
import ru.lingstra.communications.data.database.dao.UserDao
import ru.lingstra.communications.data.database.entities.QuestionEntity
import ru.lingstra.communications.data.database.entities.ResultReferenceEntity
import ru.lingstra.communications.data.database.entities.TestEntity
import ru.lingstra.communications.data.database.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TestEntity::class,
        ResultReferenceEntity::class,
        QuestionEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun testDao(): TestDao
    abstract fun resultReferenceDao(): ResultReferenceDao
    abstract fun questionDao(): QuestionDao
}