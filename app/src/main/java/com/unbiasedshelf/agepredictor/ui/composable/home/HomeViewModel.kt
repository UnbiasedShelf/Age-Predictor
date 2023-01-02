package com.unbiasedshelf.agepredictor.ui.composable.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unbiasedshelf.agepredictor.data.repository.AgifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AgifyRepository
) : ViewModel() {
    var name by mutableStateOf("")
    var age: Int? by mutableStateOf(null) // todo status

    fun getAge() {
        viewModelScope.launch {
            age = repository.getAgeByName(name)
        }
    }

    // todo some result maybe
    fun addToFavorites() {
        age?.let {
            repository.addToFavorites(name, it)
        }
    }
}