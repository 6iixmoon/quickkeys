package com.sam.quickkeys.repository

import com.sam.quickkeys.data.BookingDao
import com.sam.quickkeys.model.Booking

class BookingRepository(private val bookingDao: BookingDao) {
    suspend fun insertBooking(booking: Booking) = bookingDao.insertBooking(booking)
    suspend fun getBookingsByUser(userId: Int) = bookingDao.getBookingsByUser(userId)
}
