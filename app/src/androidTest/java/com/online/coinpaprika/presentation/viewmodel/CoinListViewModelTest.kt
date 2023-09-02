package com.online.coinpaprika.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinList
import com.online.coinpaprika.data.repository.FakeCoinRepository
import com.online.coinpaprika.domain.repository.CoinRepository
import com.online.coinpaprika.utils.ErrorCode
import com.online.coinpaprika.utils.TestData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
@HiltAndroidTest
class CoinListViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var viewModel: CoinListViewModel

    @Inject
    lateinit var repository: CoinRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun get_success_coin_list_from_view_model() = runTest {
        val coinListResponse = ServiceResponse.Success(TestData.coinList)
        val flow = viewModel.coinsState
        launch {
            val initial = flow.first()
            Assert.assertNotNull(initial)
            Assert.assertEquals(ServiceResponse.Loading, initial)
        }
        viewModel.getCoinList()
        val response = flow.drop(1).first()
        Assert.assertNotNull(response)
        Assert.assertEquals(coinListResponse.data.size,
            (response as ServiceResponse.Success).data.size)
    }

    @Test
    fun get_success_empty_coin_list_from_view_model() = runTest {
        val coinListResponse = ServiceResponse.Success(CoinList())
        (repository as FakeCoinRepository).setFakeCoinList(coinListResponse)
        val flow = viewModel.coinsState
        launch {
            val initial = flow.first()
            Assert.assertNotNull(initial)
            Assert.assertEquals(ServiceResponse.Loading, initial)
        }
        viewModel.getCoinList()
        val response = flow.drop(1).first()
        Assert.assertNotNull(response)
        Assert.assertEquals(0,
            (response as ServiceResponse.Success).data.size)
    }
    @Test
    fun get_error_from_view_model() = runTest {
        val unknownError = "Unknown error"
        val coinListResponse = ServiceResponse.
        Error(ErrorCode.UNKNOWN_ERROR.statusCode, unknownError)
        (repository as FakeCoinRepository).setFakeCoinList(coinListResponse)
        val flow = viewModel.coinsState
        launch {
            val initial = flow.first()
            Assert.assertNotNull(initial)
            Assert.assertEquals(ServiceResponse.Loading, initial)
        }
        viewModel.getCoinList()
        val response = flow.drop(1).first()
        Assert.assertNotNull(response)
        Assert.assertEquals(coinListResponse.message,
            unknownError)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}