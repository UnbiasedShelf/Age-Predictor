package com.unbiasedshelf.agepredictor.data.model

import com.google.gson.annotations.SerializedName

data class AgePrediction(
    @SerializedName("name") val name: String,
    @SerializedName("age") val age: Int?
)
