package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ru.lingstra.communications.data.database.entities.TestEntity
import ru.lingstra.communications.data.database.relations.Test

@Dao
interface TestDao : BaseDao<TestEntity> {
    @Query("SELECT * from tests WHERE id = :key")
    fun getTestById(key: String): Single<Test>
}