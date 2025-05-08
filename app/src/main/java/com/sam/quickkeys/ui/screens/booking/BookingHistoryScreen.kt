package com.sam.quickkeys.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sam.quickkeys.viewmodel.BookingViewModel

@Composable
fun BookingHistoryScreen(
    userId: Int,
    bookingViewModel: BookingViewModel = viewModel()
) {
    val bookings by bookingViewModel.bookings

    LaunchedEffect(userId) {
        bookingViewModel.fetchBookings(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Booking History",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No bookings found.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(bookings) { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Car ID: ${booking.carId}", fontWeight = FontWeight.Medium)
                            Text("Start Date: ${booking.startDate}")
                            Text("End Date: ${booking.endDate}")
                            Text("Total Price: $${booking.totalPrice}")
                        }
                    }
                }
            }
        }
    }
}
