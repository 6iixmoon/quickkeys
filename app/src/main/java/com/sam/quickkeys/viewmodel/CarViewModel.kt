package com.sam.quickkeys.viewmodel

import androidx.lifecycle.*
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.repository.CarRepository
import kotlinx.coroutines.launch

class CarViewModel(
    private val carRepository: CarRepository
) : ViewModel() {

    val allCars: LiveData<List<Car>> = carRepository.allCars

    fun addCar(car: Car) = viewModelScope.launch {
        carRepository.insertCar(car)
    }

    fun deleteCar(car: Car) = viewModelScope.launch {
        carRepository.deleteCar(car)
    }

    fun updateCar(car: Car) = viewModelScope.launch {
        carRepository.updateCar(car)
    }

    fun getCarById(id: Int): LiveData<Car> {
        return carRepository.getCarById(id).asLiveData()
    }
}
