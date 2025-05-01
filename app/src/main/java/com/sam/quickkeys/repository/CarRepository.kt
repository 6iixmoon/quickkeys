package com.sam.quickkeys.repository

import androidx.lifecycle.LiveData
import com.sam.quickkeys.data.CarDao
import com.sam.quickkeys.model.Car

class CarRepository(private val carDao: CarDao) {
    val allCars: LiveData<List<Car>> = carDao.getAllCars()

    fun getCarsByModel(model: String): LiveData<List<Car>> {
        return carDao.getCarsByModel(model)
    }

    fun getCarsByType(type: String): LiveData<List<Car>> {
        return carDao.getCarsByType(type)
    }

    fun getCarsSortedByPrice(): LiveData<List<Car>> {
        return carDao.getCarsSortedByPrice()
    }

    suspend fun insertCar(car: Car) {
        carDao.insertCar(car)
    }

    suspend fun updateCar(car: Car) {
        carDao.updateCar(car)
    }

    suspend fun deleteCar(car: Car) {
        carDao.deleteCar(car)
    }
}

