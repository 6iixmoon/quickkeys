package com.sam.quickkeys.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sam.quickkeys.model.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    // Get all cars
    @Query("SELECT * FROM cars")
    fun getAllCars(): LiveData<List<Car>>

    // Insert a car
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    // Update a car
    @Update
    suspend fun updateCar(car: Car)

    // Delete a car
    @Delete
    suspend fun deleteCar(car: Car)

    // Get cars by model
    @Query("SELECT * FROM cars WHERE model LIKE :model")
    fun getCarsByModel(model: String): LiveData<List<Car>>

    // Get cars by type (fixed to match your Car model)
    @Query("SELECT * FROM cars WHERE type LIKE :type")
    fun getCarsByType(type: String): LiveData<List<Car>>

    // Get cars sorted by price (fixed to match your Car model)
    @Query("SELECT * FROM cars ORDER BY pricePerDay ASC") // 'pricePerDay' should match the Car model
    fun getCarsSortedByPrice(): LiveData<List<Car>>

    // Get car by ID
    @Query("SELECT * FROM cars WHERE id = :id LIMIT 1")
    fun getCarById(id: Int): Flow<Car>

}
