package com.online.coinpaprika.di

import com.online.coinpaprika.data.api.ServiceEndPoints
import com.online.coinpaprika.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Used to provide fake/ mock classes with the help of Hilt
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object NetworkTestModule {

    // Creating mock Retrofit instance using MockWebServer url
    @Provides
    fun provideServiceEndPoints(
        url: HttpUrl,
        client: OkHttpClient
    ): ServiceEndPoints {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ServiceEndPoints::class.java)
    }

    // providing the MockWebServer instance
    @Provides
    fun provideMockServer(): MockWebServer {
        return MockWebServer()
    }

    // providing fake server url
    @Provides
    fun providesURL(server: MockWebServer): HttpUrl {
        return server.url("http://localhost:8080/")
    }

    // providing fake OkHttpClient for building fake retrofit builder
    @Provides
    fun provideServiceClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}