package ru.lingstra.avitocopy.system.network

import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import ru.lingstra.avitocopy.BuildConfig
import ru.lingstra.avitocopy.toothpick.SuccessInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class HttpClientProvider @Inject constructor(
    @SuccessInterceptor private val interceptor: Interceptor
) : Provider<OkHttpClient> {

    companion object {
        private const val CONNECT_TIMEOUT = 30 * 1000L
        private const val READ_TIMEOUT = 30 * 1000L
    }

    override fun get(): OkHttpClient = with(OkHttpClient.Builder()) {
        connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)


        addNetworkInterceptor(interceptor)

        if (BuildConfig.DEBUG) {
            addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }
        build()
    }
}