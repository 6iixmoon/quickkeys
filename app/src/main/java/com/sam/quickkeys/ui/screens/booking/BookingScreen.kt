package com.sam.quickkeys.ui.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sam.quickkeys.model.Booking
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun BookingScreen(
    car: Car,
    userId: Int,
    bookingViewModel: BookingViewModel = viewModel(),
    navController: NavHostController
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Book ${car.name}", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = {
                startDate = it
                errorMessage = null
            },
            label = { Text("Start Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = {
                endDate = it
                errorMessage = null
            },
            label = { Text("End Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val days = calculateDaysBetween(startDate, endDate)
                if (days == null || days <= 0) {
                    errorMessage = "Invalid dates. Ensure format is YYYY-MM-DD and end date is after start date."
                } else {
                    val total = days * car.pricePerDay
                    val booking = Booking(
                        userId = userId,
                        carId = car.id,
                        startDate = startDate,
                        endDate = endDate,
                        totalPrice = total
                    )
                    bookingViewModel.bookCar(booking)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Booking")
        }
    }
}

private fun calculateDaysBetween(start: String, end: String): Int? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = format.parse(start)
        val endDate = format.parse(end)
        if (startDate != null && endDate != null && !startDate.after(endDate)) {
            val diff = endDate.time - startDate.time
            TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
        } else null
    } catch (e: Exception) {
        null
    }
}
