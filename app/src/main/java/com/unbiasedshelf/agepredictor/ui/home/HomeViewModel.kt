package com.unbiasedshelf.agepredictor.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unbiasedshelf.agepredictor.R
import com.unbiasedshelf.agepredictor.data.repository.AgifyRepository
import com.unbiasedshelf.agepredictor.data.repository.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AgifyRepository
) : ViewModel() {
    var name by mutableStateOf("")
    var ageStatus: Status<Int>? by mutableStateOf(null)

    fun getAge() {
        viewModelScope.launch {
            ageStatus = repository.getAgeByName(name)
        }
    }

    fun addToFavorites(): Status<Unit> = if (ageStatus is Status.Success) {
        repository.addToFavorites(name, (ageStatus as Status.Success<Int>).value)
    } else {
        Status.Error(R.string.error_unable_to_add_fav)
    }
}