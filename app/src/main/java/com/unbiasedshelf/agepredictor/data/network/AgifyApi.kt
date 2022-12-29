package com.unbiasedshelf.agepredictor.data.network

import com.unbiasedshelf.agepredictor.data.model.AgePrediction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AgifyApi {
    @GET(".")
    suspend fun getAgeByName(@Query("name") name: String): Response<AgePrediction>
}