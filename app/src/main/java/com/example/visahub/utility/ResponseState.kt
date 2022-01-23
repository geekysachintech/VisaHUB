package com.example.visahub.utility

sealed class ResponseState<T>(
    val data: T? = null,
    val message: String? = null,
    val extra: String? = null,
    val signUpMessage: String? = null,
) {

    class Success<T>(data: T) : ResponseState<T>(data)
    class Error<T>(message: String) : ResponseState<T>(message = message)
    class Loading<T> : ResponseState<T>()
    class Extras<T>(extra: String) : ResponseState<T>(extra = extra)
    class SignUpMessage<T>(signUpMessage: String?): ResponseState<T>(signUpMessage = signUpMessage)

}