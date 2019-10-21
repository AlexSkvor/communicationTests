package ru.lingstra.communications.system.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.lingstra.communications.data.data_entities.GitResponse

interface NetworkApi {
    @GET("https://api.github.com/repos/AlexSkvor/communicationTests/contents/{fileName}")
    fun getFile(
        @Path("fileName") fileName: String
    ): Single<GitResponse>
}