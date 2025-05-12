package com.sam.quickkeys.viewmodel

import androidx.lifecycle.*
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.repository.CarRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class CarViewModel(
    private val carRepository: CarRepository
) : ViewModel() {

    // LiveData for all cars
    open val allCars: LiveData<List<Car>> = carRepository.allCars

    // SharedFlow for emitting action messages
    private val _carActionMessage = MutableSharedFlow<String>()
    val carActionMessage = _carActionMessage.asSharedFlow()

    // Add Car
    fun addCar(car: Car) = viewModelScope.launch {
        try {
            carRepository.insertCar(car)
            _carActionMessage.emit("Car added successfully!")
        } catch (e: Exception) {
            _carActionMessage.emit("Failed to add car.")
        }
    }

    // Delete Car
    fun deleteCar(car: Car) = viewModelScope.launch {
        try {
            carRepository.deleteCar(car)
            _carActionMessage.emit("Car deleted successfully!")
        } catch (e: Exception) {
            _carActionMessage.emit("Failed to delete car.")
        }
    }

    // Update Car
    fun updateCar(car: Car) = viewModelScope.launch {
        try {
            carRepository.updateCar(car)
            _carActionMessage.emit("Car updated successfully!")
        } catch (e: Exception) {
            _carActionMessage.emit("Failed to update car.")
        }
    }

    // Get Car by ID
    fun getCarById(id: Int): LiveData<Car> {
        return carRepository.getCarById(id).asLiveData()
    }
}
