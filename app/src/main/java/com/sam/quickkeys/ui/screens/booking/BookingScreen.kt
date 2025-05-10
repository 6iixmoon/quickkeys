package com.sam.quickkeys.ui.screens.booking

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sam.quickkeys.model.Booking
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.viewmodel.BookingViewModel
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// ----------- MAIN SCREEN ------------------

@Composable
fun BookingScreen(
    car: Car,  // Directly pass Car object instead of LiveData<Car>
    userId: Int,
    bookingViewModel: BookingViewModel,
    navController: NavHostController
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Book ${car.name} ${car.model}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = {
                startDate = it
                errorMessage = null
            },
            label = { Text("Start Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = null) },
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
            leadingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = null) },
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

// ---------- VIEWMODEL FACTORY WRAPPER ------------------

@Composable
fun BookingScreenWithFactory(
    car: Car,  // Directly pass Car object
    userId: Int,
    navController: NavHostController
) {
    val context = LocalContext.current.applicationContext as Application
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(context)
    )

    BookingScreen(
        car = car,
        userId = userId,
        bookingViewModel = bookingViewModel,
        navController = navController
    )
}

class BookingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// ------------ DATE HELPERS ---------------------

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

@Preview(showBackground = true)
@Composable
fun BookingScreenPreview() {
    val fakeCar = Car(
        id = 1,
        name = "Tesla Model S",
        model = "2023",
        type = "Electric",
        pricePerDay = 150.0,
        imageUrl = "",
        isAvailable = true,
        description = "A luxury electric sedan."
    )

    MaterialTheme {
        BookingScreenWithFactory(
            car = fakeCar,  // Directly pass the Car object
            userId = 123,
            navController = rememberNavController()
        )
    }
}
