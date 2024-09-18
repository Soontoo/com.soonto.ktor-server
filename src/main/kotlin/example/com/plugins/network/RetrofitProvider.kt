package example.com.plugins.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import example.com.AppConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


private const val TIMEOUT_SECOND = 30L

object RetrofitProvider {
    private val client =
        OkHttpClient.Builder().addInterceptor(HeaderInterceptor()).connectTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECOND, TimeUnit.SECONDS).build()
    val get: Retrofit by lazy {
        Retrofit.Builder().client(client)
            .baseUrl("https://llm.api.cloud.yandex.net")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Api-Key ${AppConfig.yandexIAMToken}")
            .addHeader("x-folder-id", AppConfig.yandexFolderId)
            .build()

        return chain.proceed(request)
    }
}