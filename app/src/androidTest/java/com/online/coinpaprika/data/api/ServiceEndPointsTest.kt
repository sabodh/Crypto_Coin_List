package com.online.coinpaprika.data.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.online.coinpaprika.utils.Helper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class ServiceEndPointsTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var serviceEndPoints: ServiceEndPoints

    @Inject
    lateinit var mockServer: MockWebServer

    @Before
    fun setUp() {
        hiltRule.inject()
        mockServer.start(8080)
    }

    @Test
    fun get_success_coin_list_from_server() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/Coins.json")
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody(content)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(4, response.body()?.size)
    }

    @Test
    fun get_success_coin_empty_list_from_server() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody("[]")
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
        assertTrue(response.body()!!.isEmpty())
        assertEquals(0, response.body()?.size)
    }

    @Test
    fun get_success_coin_details_from_server() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/CoinDetails.json")
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody(content)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoinDetails("btc-bitcoin")
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
    }

    @Test
    fun get_success_coin_empty_details_from_server() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody("{}")
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoinDetails("btc-bitcoins")
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
        assertNull(response.errorBody())
    }

    // HTTP error cases
    @Test
    fun get_500_error_coin_list_from_server() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(false, response.isSuccessful)
        assertNotNull(response.errorBody())
        assertEquals(response.code(), HttpURLConnection.HTTP_INTERNAL_ERROR)
        assertEquals(response.message(), "Server Error")
    }

    @Test
    fun get_500_error_coin_details_from_server() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoinDetails("btc-bitcoin")
        mockServer.takeRequest()
        assertEquals(false, response.isSuccessful)
        assertNotNull(response.errorBody())
        assertEquals(response.code(), HttpURLConnection.HTTP_INTERNAL_ERROR)
        assertEquals(response.message(), "Server Error")
    }

    @Test
    fun get_404_error_coin_list_from_server() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(false, response.isSuccessful)
        assertNotNull(response.errorBody())
        assertNull(response.body())
        assertEquals(response.code(), HttpURLConnection.HTTP_NOT_FOUND)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}