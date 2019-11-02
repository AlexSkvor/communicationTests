package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ru.lingstra.communications.data.database.entities.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * from users")
    fun getAll(): Single<List<UserEntity>>
}