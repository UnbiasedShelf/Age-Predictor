package com.unbiasedshelf.agepredictor.data.repository

import android.content.SharedPreferences
import com.unbiasedshelf.agepredictor.R
import com.unbiasedshelf.agepredictor.data.model.AgePrediction
import com.unbiasedshelf.agepredictor.data.network.AgifyService
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AgifyRepository @Inject constructor(
    private val favorites: SharedPreferences,
    private val agifyService: AgifyService
) {
    suspend fun getAgeByName(name: String): Status<Int> = try {
        if (name.isBlank()) {
            Status.Error(R.string.error_blank_name)
        } else {
            val result = agifyService.getAgeByName(name)
            val age = result.body()?.age
            when {
                result.code() == 429 -> Status.Error(R.string.error_too_many_requests)
                result.isSuccessful && age == null -> Status.Error(R.string.error_name_not_found)
                result.isSuccessful && age != null -> Status.Success(age)
                else -> Status.Error(R.string.error_unexpected)
            }
        }
    } catch (e: UnknownHostException) {
        Status.Error(R.string.error_unable_to_resolve_host)
    } catch (e: SocketTimeoutException) {
        Status.Error(R.string.error_timeout)
    } catch (e: Exception) {
        Status.Error(R.string.error_unexpected)
    }

    fun getFavorites(): Status<List<AgePrediction>> = try {
        val result = favorites.all.map {
            AgePrediction(it.key, it.value as? Int)
        }
        Status.Success(result)
    } catch (e: NullPointerException) {
        Status.Error(R.string.error_unable_to_load_fav)
    }

    fun addToFavorites(name: String, age: Int): Status<Unit> {
        val isSuccessful = favorites
            .edit()
            .putInt(name, age)
            .commit()

        return if (isSuccessful)
            Status.Success(Unit, R.string.add_fav_success)
        else
            Status.Error(R.string.error_unable_to_add_fav)
    }

    fun removeFromFavorites(names: List<String>): Status<Unit> {
        val isSuccessful = favorites
            .edit()
            .apply {
                names.forEach { remove(it) }
            }
            .commit()

        return if (isSuccessful)
            Status.Success(Unit, R.string.remove_fav_success)
        else
            Status.Error(R.string.error_unable_to_remove_fav)
    }
}