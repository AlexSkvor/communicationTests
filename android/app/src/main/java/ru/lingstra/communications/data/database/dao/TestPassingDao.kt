package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ru.lingstra.communications.data.database.entities.TestPassingEntity

@Dao
interface TestPassingDao : BaseDao<TestPassingEntity> {

    @Query("SELECT * FROM test_passings WHERE userId = :userId")
    fun getTestPassingsForUser(userId: String): Single<List<TestPassingEntity>>
}