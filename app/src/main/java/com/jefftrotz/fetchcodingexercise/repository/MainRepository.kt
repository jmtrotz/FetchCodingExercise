package com.jefftrotz.fetchcodingexercise.repository

import com.jefftrotz.fetchcodingexercise.model.Result
import com.jefftrotz.fetchcodingexercise.model.remote.NetworkService
import com.jefftrotz.fetchcodingexercise.util.ListUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepository(
    private val networkService: NetworkService
) {
    suspend fun getRemoteData() = flow {
        emit(Result.Loading())
        val response = networkService.getRemoteData()
        if (response.isSuccessful) {
            var responseBody = response.body()
            responseBody = ListUtils().cleanUpList(responseBody!!)
            emit(Result.Success(responseBody))
        } else {
            emit(Result.Error(response.message()))
        }
    }.flowOn(Dispatchers.IO)
}