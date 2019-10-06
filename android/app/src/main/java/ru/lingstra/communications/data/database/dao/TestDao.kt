package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import ru.lingstra.communications.data.database.entities.TestEntity

@Dao
interface TestDao : BaseDao<TestEntity> {
}