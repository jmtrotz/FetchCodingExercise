package com.jefftrotz.fetchcodingexercise.model.remote

import com.jefftrotz.fetchcodingexercise.model.Item
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("hiring.json")
    fun getRemoteData(): Response<List<Item>>
}