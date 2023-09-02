package com.online.coinpaprika.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.domain.usecases.CoinDetailsUseCase
import com.online.coinpaprika.utils.Constants
import com.online.coinpaprika.utils.ErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View-model used to get the coin details
 */
@HiltViewModel
class CoinDetailsViewModel @Inject constructor  (
    private val coinDetailsUseCase: CoinDetailsUseCase
) : ViewModel() {
    private val _coinDetailsState =
        MutableStateFlow<ServiceResponse<CoinDetails>>(ServiceResponse.Loading)
    val coinDetailsState : StateFlow<ServiceResponse<CoinDetails>>
        get() = _coinDetailsState


    /**
     * Get the coin details from server based on coinId.
     */
    fun getCoinDetails(coinId: String) {
        viewModelScope.launch {
            coinDetailsUseCase(coinId).catch { e ->
                _coinDetailsState.value = ServiceResponse.Error(
                    ErrorCode.UNKNOWN_ERROR.statusCode,
                    e.message ?: Constants.COMMON_ERROR_MESSAGE
                )
            }.collect {
                _coinDetailsState.value = it
            }
        }
    }
}