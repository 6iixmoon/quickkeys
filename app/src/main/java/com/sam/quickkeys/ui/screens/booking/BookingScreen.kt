package com.sam.quickkeys.ui.screens.booking

import android.app.Application
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sam.quickkeys.model.Booking
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.viewmodel.BookingViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun BookingScreen(
    car: Car,
    userId: Int,
    bookingViewModel: BookingViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val calendar = Calendar.getInstance()

    fun openDatePicker(onDateSelected: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selected = "%04d-%02d-%02d".format(year, month + 1, day)
                onDateSelected(selected)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Book ${car.name} ${car.model}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = startDate,
                onValueChange = { },
                label = { Text("Start Date") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    IconButton(onClick = { openDatePicker { startDate = it; errorMessage = null } }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = endDate,
                onValueChange = { },
                label = { Text("End Date") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    IconButton(onClick = { openDatePicker { endDate = it; errorMessage = null } }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = null)
                    }
                }
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            val coroutineScope = rememberCoroutineScope()

            Button(
                onClick = {
                    val days = calculateDaysBetween(startDate, endDate)
                    if (days == null || days <= 0) {
                        errorMessage = "Invalid dates. Ensure start date is before end date."
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

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Booking confirmed: ${car.name} from $startDate to $endDate")
                        }

                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirm Booking")
            }

        }
    }
}

@Composable
fun BookingScreenWithFactory(
    car: Car,
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
