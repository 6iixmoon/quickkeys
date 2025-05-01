package com.sam.quickkeys.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sam.quickkeys.data.AppDatabase
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.repository.CarRepository
import kotlinx.coroutines.launch

class CarViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CarRepository
    val allCars: LiveData<List<Car>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CarRepository(db.carDao())
        allCars = repository.allCars
    }

    fun addCar(car: Car) = viewModelScope.launch {
        repository.insertCar(car)
    }

    fun deleteCar(car: Car) = viewModelScope.launch {
        repository.deleteCar(car)
    }

    fun updateCar(car: Car) = viewModelScope.launch {
        repository.updateCar(car)
    }


}


