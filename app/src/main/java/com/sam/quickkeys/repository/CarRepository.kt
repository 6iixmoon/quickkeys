package com.sam.quickkeys.repository

import androidx.lifecycle.LiveData
import com.sam.quickkeys.data.CarDao
import com.sam.quickkeys.model.Car
import kotlinx.coroutines.flow.Flow

class CarRepository(private val carDao: CarDao) {

    // Get all cars
    val allCars: LiveData<List<Car>> = carDao.getAllCars()

    // Get cars by model
    fun getCarsByModel(model: String): LiveData<List<Car>> {
        return carDao.getCarsByModel(model)
    }

    // Get cars by type
    fun getCarsByType(type: String): LiveData<List<Car>> {
        return carDao.getCarsByType(type)
    }

    // Get car by ID (fix the argument type)
    fun getCarById(id: Int): Flow<Car> {
        return carDao.getCarById(id)
    }


    // Get cars sorted by price
    fun getCarsSortedByPrice(): LiveData<List<Car>> {
        return carDao.getCarsSortedByPrice()
    }

    // Insert a car
    suspend fun insertCar(car: Car) {
        carDao.insertCar(car)
    }

    // Update a car
    suspend fun updateCar(car: Car) {
        carDao.updateCar(car)
    }

    // Delete a car
    suspend fun deleteCar(car: Car) {
        carDao.deleteCar(car)
    }
}
