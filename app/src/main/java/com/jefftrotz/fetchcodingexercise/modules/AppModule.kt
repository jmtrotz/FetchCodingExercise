package com.jefftrotz.fetchcodingexercise.modules

import com.jefftrotz.fetchcodingexercise.Constants
import com.jefftrotz.fetchcodingexercise.model.remote.NetworkService
import com.jefftrotz.fetchcodingexercise.repository.MainRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
    single {
        MainRepository(get())
    }
}