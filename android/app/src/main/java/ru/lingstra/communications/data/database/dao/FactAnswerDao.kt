package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ru.lingstra.communications.data.database.entities.FactAnswerEntity

@Dao
interface FactAnswerDao : BaseDao<FactAnswerEntity> {

    @Query("SELECT * FROM fact_answers WHERE passingId = :passingId")
    fun getAnswersForPassing(passingId: String): Single<List<FactAnswerEntity>>
}