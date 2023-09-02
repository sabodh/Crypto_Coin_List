package com.online.coinpaprika.data.repository

import com.online.coinpaprika.data.api.ServiceEndPoints
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.api.ServiceResponse.*
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.data.model.CoinList
import com.online.coinpaprika.domain.repository.CoinRepository
import com.online.coinpaprika.utils.Constants.COMMON_ERROR_MESSAGE
import com.online.coinpaprika.utils.ErrorCode.*
import com.online.coinpaprika.utils.Constants.DISPLAY_LIST_ITEMS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Used to get the coin details
 */
class CoinRepositoryImpl @Inject constructor (
    private val serviceEndPoints: ServiceEndPoints,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoinRepository {
    /**
     * Get all the coin details, combining two api calls
     */
    override suspend fun getCoins(): Flow<ServiceResponse<CoinList>> {
        return flow {
            try {
                val result = serviceEndPoints.getCoins()
                if (result.isSuccessful) {
                    result.body()?.let { coinList ->
                        // reduce the list size to the mentioned size, its more than 6077 items in the list
                        val limitedList = coinList.take(DISPLAY_LIST_ITEMS)
                        // used to store new lists
                        val mergedCoinList = CoinList()
                        limitedList.forEach { coinItem ->
                            val detailsResponse = serviceEndPoints.getCoinDetails(coinItem.id)
                            if (detailsResponse.isSuccessful) {
                                detailsResponse.body()?.let { coinDetails ->
                                    mergedCoinList.add(coinDetails)
                                } ?: emit(Error(UNKNOWN_ERROR.statusCode, COMMON_ERROR_MESSAGE))
                            } else {
                                emit(
                                    Error(
                                        result.code(),
                                        result.errorBody()?.string() ?: COMMON_ERROR_MESSAGE
                                    )
                                )
                            }
                        }
                        emit(Success(mergedCoinList))
                    }
                } else {
                    emit(Error(result.code(), result.errorBody()?.string() ?: COMMON_ERROR_MESSAGE))
                }
            } catch (e: UnknownHostException) {
                emit(Error(NETWORK_ERROR.statusCode, e.message ?: COMMON_ERROR_MESSAGE))
            } catch (exception: java.lang.Exception) {
                emit(Error(EXCEPTION.statusCode, exception.message ?: COMMON_ERROR_MESSAGE))
            }

        }.flowOn(defaultDispatcher)
    }

    /**
     * Get the coin details based on coinId
     */
    override suspend fun getCoinDetails(coinId: String): Flow<ServiceResponse<CoinDetails>> {
        return flow {
            try {
                val result = serviceEndPoints.getCoinDetails(coinId)
                if (result.isSuccessful) {
                    result.body()?.let { coinDetails ->
                        emit(Success(coinDetails))
                    } ?: emit(Error(UNKNOWN_ERROR.statusCode, COMMON_ERROR_MESSAGE))
                } else {
                    emit(Error(result.code(), result.errorBody()?.string() ?: COMMON_ERROR_MESSAGE))
                }
            } catch (e: UnknownHostException) {
                emit(Error(NETWORK_ERROR.statusCode, e.message ?: COMMON_ERROR_MESSAGE))
            } catch (exception: java.lang.Exception) {
                emit(Error(EXCEPTION.statusCode, exception.message ?: COMMON_ERROR_MESSAGE))
            }
        }.flowOn(defaultDispatcher)
    }

}