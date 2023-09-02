package com.online.coinpaprika.di
import com.online.coinpaprika.data.repository.FakeCoinRepository
import com.online.coinpaprika.domain.repository.CoinRepository
import com.online.coinpaprika.domain.usecases.CoinDetailsUseCase
import com.online.coinpaprika.domain.usecases.CoinListUseCase
import com.online.coinpaprika.presentation.viewmodel.CoinDetailsViewModel
import com.online.coinpaprika.presentation.viewmodel.CoinListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * Providing fake classes for instrument testing using hilt,
 * it replaces original modules with fake objects mentioned in the app
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class, UsecasesModule::class]
)
object RepositoryTestModule {

    // Providing fake repository object
    @Provides
    @Singleton
    fun provideCoinRepository(dispatcher: CoroutineDispatcher): CoinRepository {
        return FakeCoinRepository(dispatcher)
    }
    // Providing use cases with fake repository object
    @Provides
    fun provideCoinListUseCases(coinRepository: CoinRepository): CoinListUseCase {
        return CoinListUseCase(coinRepository)
    }
    // Providing use cases with fake repository object
    @Provides
    fun provideCoinDetailsUseCases(coinRepository: CoinRepository): CoinDetailsUseCase {
        return CoinDetailsUseCase(coinRepository)
    }
    // Providing view-model with fake use-case object
    @Provides
    fun provideCoinListViewModel(coinListUseCase: CoinListUseCase): CoinListViewModel {
        return CoinListViewModel(coinListUseCase)
    }
    // Providing view-model with fake use-case object
    @Provides
    fun provideCoinDetailsViewModel(coinDetailsUseCase: CoinDetailsUseCase): CoinDetailsViewModel {
        return CoinDetailsViewModel(coinDetailsUseCase)
    }

    // Providing CoroutineDispatcher object
    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO;
    }
}