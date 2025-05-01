package com.sam.quickkeys.ui.screens.booking

@Composable
fun BookingScreen(
    car: Car,
    userId: Int,
    bookingViewModel: BookingViewModel = viewModel(),
    navController: NavHostController
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Book ${car.name}", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Start Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("End Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val days = calculateDaysBetween(startDate, endDate)
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
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Booking")
        }
    }
}

fun calculateDaysBetween(start: String, end: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val startDate = LocalDate.parse(start, formatter)
    val endDate = LocalDate.parse(end, formatter)
    return ChronoUnit.DAYS.between(startDate, endDate).toInt().coerceAtLeast(1)
}
