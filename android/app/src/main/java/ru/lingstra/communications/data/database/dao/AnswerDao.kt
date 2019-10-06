package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import ru.lingstra.communications.data.database.entities.AnswerEntity

@Dao
interface AnswerDao: BaseDao<AnswerEntity> {
}