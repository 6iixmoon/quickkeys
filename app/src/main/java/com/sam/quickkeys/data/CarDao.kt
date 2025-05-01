package com.sam.quickkeys.data


import androidx.lifecycle.LiveData
import androidx.room.*
import com.sam.quickkeys.model.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAllCars(): LiveData<List<Car>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    @Update
    suspend fun updateCar(car: Car)

    @Delete
    suspend fun deleteCar(car: Car)

    @Query("SELECT * FROM cars WHERE model LIKE :model")
    fun getCarsByModel(model: String): LiveData<List<Car>>

    @Query("SELECT * FROM cars WHERE type LIKE :type")
    fun getCarsByType(type: String): LiveData<List<Car>>

    @Query("SELECT * FROM cars ORDER BY price_per_day ASC")
    fun getCarsSortedByPrice(): LiveData<List<Car>>
}