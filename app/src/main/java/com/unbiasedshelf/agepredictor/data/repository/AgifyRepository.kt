package com.unbiasedshelf.agepredictor.data.repository

import android.content.SharedPreferences
import com.unbiasedshelf.agepredictor.data.model.AgePrediction
import com.unbiasedshelf.agepredictor.data.network.AgifyService
import javax.inject.Inject

class AgifyRepository @Inject constructor(
    private val favoriteSharedPreferences: SharedPreferences,
    private val agifyService: AgifyService
) {
    suspend fun getAgeByName(name: String): Int? {
        // todo cache & cover with status
        return agifyService.getAgeByName(name).body()?.age
    }

    // todo maybe only names
    fun getFavorites(): List<AgePrediction> {
        return favoriteSharedPreferences.all.map {
            AgePrediction(it.key, it.value as? Int)
        }
    }

    fun addToFavorites(name: String, age: Int): Boolean {
        return favoriteSharedPreferences.edit().putInt(name, age).commit()
    }

    fun removeFromFavorites(names: List<String>): Boolean {
        return favoriteSharedPreferences
            .edit()
            .apply {
                names.forEach { remove(it) }
            }
            .commit()
    }
}