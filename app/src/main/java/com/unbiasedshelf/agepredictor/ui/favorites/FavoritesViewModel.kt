package com.unbiasedshelf.agepredictor.ui.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.unbiasedshelf.agepredictor.data.model.AgePrediction
import com.unbiasedshelf.agepredictor.data.repository.AgifyRepository
import com.unbiasedshelf.agepredictor.data.repository.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: AgifyRepository
) : ViewModel() {
    var favorites by mutableStateOf<Status<List<AgePrediction>>>(Status.Success(emptyList()))

    fun getFavorites() {
        favorites = repository.getFavorites()
    }

    fun removeFromFavorites(names: List<String>): Status<Unit> = repository.removeFromFavorites(names)
}