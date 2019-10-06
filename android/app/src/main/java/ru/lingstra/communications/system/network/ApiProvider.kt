package ru.lingstra.communications.system.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.lingstra.communications.toothpick.DefaultServerPath
import ru.lingstra.communications.toothpick.SuccessOkHttpClient
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(
    @SuccessOkHttpClient private val okHttpClient: OkHttpClient,
    @DefaultServerPath private val serverPath: String
) : Provider<NetworkApi> {
    override fun get(): NetworkApi = with(Retrofit.Builder()) {
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        callbackExecutor(Executors.newFixedThreadPool(4))
        baseUrl(serverPath)
        Timber.e(serverPath)
        client(okHttpClient)
        build()
    }.create(NetworkApi::class.java)
}