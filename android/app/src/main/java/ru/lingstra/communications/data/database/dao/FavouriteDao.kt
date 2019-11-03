package ru.lingstra.communications.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import ru.lingstra.communications.data.database.entities.FavouriteEntity

@Dao
interface FavouriteDao : BaseDao<FavouriteEntity> {

    @Query("SELECT testId FROM favourites WHERE userId = :userId")
    fun getUsersFavouritesList(userId: String): Single<List<String>>
}