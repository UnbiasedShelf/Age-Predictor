package com.unbiasedshelf.agepredictor.ui.composable.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unbiasedshelf.agepredictor.data.network.AgifyService
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var name by mutableStateOf("")
    var age: Int? by mutableStateOf(21) // todo null

    //todo add repository layer and exception handling
    fun getAge() {
        viewModelScope.launch {
            age = AgifyService.agifyApi.getAgeByName(name).body()?.age
        }
    }
}