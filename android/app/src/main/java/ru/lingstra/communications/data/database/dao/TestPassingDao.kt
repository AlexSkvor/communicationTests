package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import ru.lingstra.communications.data.database.entities.TestPassingEntity

@Dao
interface TestPassingDao: BaseDao<TestPassingEntity> {
}