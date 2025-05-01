package com.sam.quickkeys.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sam.quickkeys.data.AppDatabase
import com.sam.quickkeys.model.Booking
import com.sam.quickkeys.repository.BookingRepository
import kotlinx.coroutines.launch


class BookingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookingRepository
    private var _bookings = mutableStateOf<List<Booking>>(emptyList())
    val bookings: State<List<Booking>> get() = _bookings


    init {
        val db = AppDatabase.getDatabase(application)
        repository = BookingRepository(db.bookingDao())
    }

    fun fetchBookings(userId: Int) = viewModelScope.launch {
        _bookings.value = repository.getBookingsByUser(userId)
    }

    fun bookCar(booking: Booking) = viewModelScope.launch {
        repository.insertBooking(booking)
        fetchBookings(booking.userId)
    }
}
