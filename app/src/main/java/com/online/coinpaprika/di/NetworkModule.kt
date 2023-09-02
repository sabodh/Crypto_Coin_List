package com.online.coinpaprika.di

import com.online.coinpaprika.data.api.ServiceEndPoints
import com.online.coinpaprika.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Module used to provide the dependencies needed for hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // provide OkHttpClient for Retrofit creation
    @Provides
    @Singleton
    fun provideServiceEndPoints(client: OkHttpClient): ServiceEndPoints {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceEndPoints::class.java)
    }

    // provide OkHttpClient for ServiceEndPoints creation
    @Provides
    @Singleton
    fun provideServiceClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}