package com.rowaad.app.di


import android.util.Log
import com.google.gson.GsonBuilder
import com.rowaad.app.data.BuildConfig
import com.rowaad.app.data.BuildConfig.DEBUG
import com.rowaad.app.data.cache.PreferencesGateway
import com.rowaad.app.data.remote.UserApi
import com.rowaad.app.data.utils.Constants_Api.BASE_TOKEN
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.DEVICE_ID
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.LANG
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.TOKEN
import com.rowaad.app.data.utils.Constants_Api.PrefKeys.TOKENS
import com.rowaad.app.data.utils.Constants_Api.VARIANT_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,gateway: PreferencesGateway): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addNetworkInterceptor { chain ->

            val devId=gateway.load(DEVICE_ID, UUID.randomUUID().toString())
            val lang = gateway.load(LANG,"ar") ?: "ar"
            Log.e("deviceId",devId.toString())
            val token = gateway.load(TOKEN, BASE_TOKEN) ?: BASE_TOKEN
            if (DEBUG) Log.e("token",token)

            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)

            //requestBuilder.addHeader("Accept", "application/json")
            //content_type='multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW'

            //requestBuilder.addHeader("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
            requestBuilder.addHeader("LOCALE-CODE", lang)
            requestBuilder.addHeader("DEVICE-ID", devId!!)
            requestBuilder.addHeader("Authorization", token)
            requestBuilder.addHeader("os", "android")

            val request = requestBuilder
                .build()
            return@addNetworkInterceptor chain.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(loggingInterceptor)
        }
        okHttpClient.build()
        return okHttpClient.build()
    }
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create(GsonBuilder().serializeNulls().create())
    }
    @Provides
    fun providesBaseUrl(): String {
        return VARIANT_URL
    }

    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient, baseUrl: String, converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideWeatherApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}