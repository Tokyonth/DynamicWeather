package com.tokyonth.weather.network

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.tokyonth.weather.base.BaseViewModel
import com.tokyonth.weather.network.error.ApiError
import com.tokyonth.weather.network.error.AppException

fun <T : BaseResponse> BaseViewModel.requestResult(
    apiBlock: suspend () -> T,
    onSuccess: (T) -> Unit,
    onFinished: (() -> Unit)? = null,
    onError: ((AppException) -> Unit)? = null
) {
    viewModelScope.launch {
        runCatching {
            apiBlock()
        }.onSuccess {
            if (it.isSuccess()) {
                onSuccess(it)
            } else {
                if (onError != null) {
                    onError(AppException(ApiError.PARSE_ERROR))
                }
            }
            onFinished?.invoke()
        }.onFailure {
            if (onError != null) {
                onError(AppException("未知错误", it))
            }
            onFinished?.invoke()
        }
    }

}
