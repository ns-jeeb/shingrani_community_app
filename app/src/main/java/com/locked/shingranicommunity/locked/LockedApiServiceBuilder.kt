package com.locked.shingranicommunity.locked

import com.locked.shingranicommunity.BuildConfig
import com.locked.shingranicommunity.session.Session
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LockedApiServiceBuilder(private val session: Session) {

    fun build() : LockedApiService {
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        builder.addInterceptor { chain: Interceptor.Chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.addHeader("appid", BuildConfig.LOCKEDAPI_APP_ID)
            if (session.getToken().isNotEmpty()) {
                requestBuilder.addHeader("x-access-token", session.getToken())
            }
            chain.proceed(requestBuilder.build())
        }
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.LOCKEDAPI_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(LockedApiService::class.java)
    }
}