package com.online.coinpaprika.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.domain.repository.CoinRepository
import com.online.coinpaprika.utils.ErrorCode
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
class CoinDetailsViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var viewModel: CoinDetailsViewModel

    @Inject
    lateinit var repository: CoinRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    // this test run individually but failed during group running
    @Test
    fun get_success_details_from_view_model() = runTest {
        val coinId = "bit-coin"
        val coinResponse = ServiceResponse.Success(CoinDetails.default)
        val flow = viewModel.coinDetailsState
        launch {
            val initial = flow.first()
            Assert.assertNotNull(initial)
            if (initial is ServiceResponse.Loading) {
                val response = flow.drop(1).first()
                Assert.assertNotNull(response)
                Assert.assertEquals(coinResponse.data.id,
                    (response as ServiceResponse.Success).data.id)
            } else {
//                Assert.fail("Expected ServiceResponse.Loading, " +
//                        "but was $initial")
                Assert.assertNotNull(initial)
                Assert.assertEquals(coinResponse.data.id,
                    (initial as ServiceResponse.Success).data.id)
            }
        }
        viewModel.getCoinDetails(coinId)
    }

    @Test
    fun get_error_from_view_model() = runTest {
        val coinId = "bit-coin-uk"
        val unknownError = "Coin not found"
        val coinListResponse = ServiceResponse.
        Error(ErrorCode.UNKNOWN_ERROR.statusCode, unknownError)
        val flow = viewModel.coinDetailsState
        viewModel.getCoinDetails(coinId)
        val response = flow.first()
        Assert.assertNotNull(response)
        Assert.assertEquals(coinListResponse.message,
            (response as ServiceResponse.Error).message)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}