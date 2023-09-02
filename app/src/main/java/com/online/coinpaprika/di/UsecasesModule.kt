package com.online.coinpaprika.di

import com.online.coinpaprika.domain.repository.CoinRepository
import com.online.coinpaprika.domain.usecases.CoinDetailsUseCase
import com.online.coinpaprika.domain.usecases.CoinListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UsecasesModule {

    @Provides
    fun provideCoinListUseCases(coinRepository: CoinRepository): CoinListUseCase{
        return CoinListUseCase(coinRepository)
    }
    @Provides
    fun provideCoinDetailsUseCases(coinRepository: CoinRepository): CoinDetailsUseCase{
        return CoinDetailsUseCase(coinRepository)
    }

}