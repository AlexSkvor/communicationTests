package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ru.lingstra.communications.data.database.entities.AnswerEntity

@Dao
interface AnswerDao : BaseDao<AnswerEntity> {

    @Query("SELECT * FROM answers WHERE id = :answerId")
    fun getAnswerById(answerId: String): Single<AnswerEntity>
}