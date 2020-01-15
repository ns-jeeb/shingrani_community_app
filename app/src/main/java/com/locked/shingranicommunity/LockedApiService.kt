package com.locked.shingranicommunity

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LockedApiService  {

    var LOCAL_BASE_URL = "http://localhost:9000"
    var BASE_URL = "https://community-eve.herokuapp.com/"
    var NEW_BASE_URL = "https://pure-river-66033-dev.herokuapp.com"
    var mRetrofit: Retrofit? = null


    fun getClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS

        if (BuildConfig.DEBUG) {
            // development build
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor(
                interceptor
                //                    new Interceptor() {
                //                @Override
                //                public Response intercept(Chain chain) throws IOException {
                //                    Request request = chain.request().newBuilder().addH
                //                    return null;
                //                }
                //            }
            )
                .addNetworkInterceptor(StethoInterceptor())
                .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES)
                .build()


            mRetrofit = Retrofit.Builder().baseUrl(NEW_BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()

        } else {
            // production build
            interceptor.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES)
                .build()

            mRetrofit = Retrofit.Builder().baseUrl(NEW_BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }

        return mRetrofit!!
    }

    fun getClient(token: String): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS

        if (BuildConfig.DEBUG) {
            // development build
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
                chain.proceed(request)
            }
                .addNetworkInterceptor(StethoInterceptor())
                .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES)
                .build()

            mRetrofit = Retrofit.Builder().baseUrl(NEW_BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()

        } else {
            // production build
            interceptor.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.MINUTES).readTimeout(30, TimeUnit.MINUTES)
                .build()

            mRetrofit = Retrofit.Builder().baseUrl(NEW_BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }

        return mRetrofit!!
    }
}