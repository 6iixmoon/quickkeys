package com.sam.quickkeys.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sam.quickkeys.model.Booking

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE userId = :userId")
    suspend fun getBookingsByUser(userId: Int): List<Booking>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)
}
