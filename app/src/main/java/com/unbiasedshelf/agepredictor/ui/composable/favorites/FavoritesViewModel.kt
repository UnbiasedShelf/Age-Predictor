package com.unbiasedshelf.agepredictor.ui.composable.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.unbiasedshelf.agepredictor.data.model.AgePrediction
import com.unbiasedshelf.agepredictor.data.repository.AgifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: AgifyRepository
) : ViewModel() {
    var favorites by mutableStateOf<List<AgePrediction>>(emptyList())

    fun getFavorites() {
        favorites = repository.getFavorites()
    }

    fun removeFromFavorites(names: List<String>): Boolean {
        val isSuccessful = repository.removeFromFavorites(names)
        return isSuccessful
    }
}