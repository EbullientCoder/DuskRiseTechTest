package com.example.techtest.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techtest.model.Result
import com.example.techtest.network.ServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {
    var resultsList: List<Result>? by mutableStateOf(listOf())
    //var errorMessage: String by mutableStateOf("")

    fun getResults(){
        //Is needed a Network Call to fetch the Json File, and a Network Call can't be executed on
        //the main thread cause it could block the UI
        viewModelScope.launch(Dispatchers.IO) {
            //The ServiceProvider will provide the functions needed to make a connection a get the Json file
            val serviceProvider = ServiceProvider.getInstance()

            resultsList = serviceProvider.fetchResults()
        }
    }
}

