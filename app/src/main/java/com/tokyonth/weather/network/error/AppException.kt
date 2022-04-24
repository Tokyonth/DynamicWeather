package com.tokyonth.weather.network.error

class AppException : Exception {

    var errorMsg: String
    var errorCode: Int = 0
    var errorLog: String?
    var throwable: Throwable? = null

    constructor(
        error: String?,
        throwable: Throwable? = null
    ) : super(error) {
        this.errorMsg = error ?: "请求失败，请稍后再试"
        this.errorCode = 500
        this.errorLog = "服务器错误"
        this.throwable = throwable
    }

    constructor(
        apiError: ApiError,
        throwable: Throwable? = null
    ) {
        this.errorCode = apiError.getKey()
        this.errorMsg = apiError.getValue()
        this.errorLog = throwable?.message
        this.throwable = throwable
    }

}
