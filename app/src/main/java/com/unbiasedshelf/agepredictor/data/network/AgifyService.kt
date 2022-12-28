package com.unbiasedshelf.agepredictor.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AgifyService {
    private const val BASE_URL = "https://api.agify.io/"

    val agifyApi: AgifyApi = getRetrofit().create(AgifyApi::class.java)

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}