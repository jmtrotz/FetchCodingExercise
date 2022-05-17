package com.jefftrotz.fetchcodingexercise.model

sealed class Result<T> {
    class Loading<T>: Result<T>()
    class Success<T>(val data: T): Result<T>()
    class Error<T>(val message: String): Result<T>()

    fun value(): T? = when (this) {
        is Success -> this.data
        else -> null
    }
}