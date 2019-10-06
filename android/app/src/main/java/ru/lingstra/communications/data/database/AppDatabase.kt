package ru.lingstra.communications.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.lingstra.communications.data.database.dao.*
import ru.lingstra.communications.data.database.entities.*

@Database(
    entities = [
        UserEntity::class,
        TestEntity::class,
        ResultReferenceEntity::class,
        QuestionEntity::class,
        AnswerEntity::class,
        TestPassingEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun testDao(): TestDao
    abstract fun resultReferenceDao(): ResultReferenceDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao
    abstract fun testPassingDao(): TestPassingDao
}