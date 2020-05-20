package com.locked.shingranicommunity.di2.locked

import com.locked.shingranicommunity.BuildConfig
import com.locked.shingranicommunity.di2.AppScope
import com.locked.shingranicommunity.locked.LockedApiService
import com.locked.shingranicommunity.session.Session
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class LockedApiServiceModule {

    @AppScope
    @Provides
    fun provideLockedApiService(session: Session): LockedApiService {
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
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