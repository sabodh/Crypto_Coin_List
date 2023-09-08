package com.online.coinpaprika.di
import com.online.coinpaprika.data.api.ServiceEndPoints
import com.online.coinpaprika.data.repository.CoinRepositoryImpl
import com.online.coinpaprika.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Module used to provide the repository dependencies needed for hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // Provide CoroutineDispatcher type

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    // Provide CoinRepositoryImpl instance
    @Provides
    fun provideCoinRepository(
        serviceEndPoints: ServiceEndPoints,
        dispatcher: CoroutineDispatcher
    ): CoinRepository {
        return CoinRepositoryImpl(serviceEndPoints, dispatcher)
    }
}