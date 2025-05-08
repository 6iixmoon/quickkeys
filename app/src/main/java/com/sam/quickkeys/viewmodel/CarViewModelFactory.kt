package com.sam.quickkeys.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sam.quickkeys.repository.CarRepository

class CarViewModelFactory(
    private val carRepository: CarRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
            return CarViewModel(carRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
