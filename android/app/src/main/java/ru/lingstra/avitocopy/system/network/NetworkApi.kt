package ru.lingstra.avitocopy.system.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lingstra.avitocopy.data.repository.IdFromRefServerResponse
import ru.lingstra.avitocopy.domain.hand_shakes.FriendsServerAnswer

interface NetworkApi {

    @GET("friends.get")
    fun friendsList(
        @Query("user_id") userId: Int,
        @Query("access_token") token: String,
        @Query("count") count: Int = 15000,
        @Query("fields") fields: String = "domain, photo_100",
        @Query("name_case") locale: String = "ru",
        @Query("v") version: String = "5.101"
    ): Single<FriendsServerAnswer>

    @GET("users.get")
    fun userInfo(
        @Query("user_ids") userId: String,
        @Query("access_token") token: String,
        @Query("lang") lang: Int = 0,
        @Query("fields") fields: String = "photo_100",
        @Query("v") version: String = "5.101"
    ): Single<IdFromRefServerResponse>
}