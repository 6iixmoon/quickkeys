package com.sam.quickkeys.ui.screens.booking

@Composable
fun BookingHistoryScreen(
    userId: Int,
    bookingViewModel: BookingViewModel = viewModel()
) {
    val bookings by bookingViewModel.bookings

    LaunchedEffect(userId) {
        bookingViewModel.fetchBookings(userId)
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(bookings) { booking ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Car ID: ${booking.carId}")
                    Text("From: ${booking.startDate}")
                    Text("To: ${booking.endDate}")
                    Text("Total: $${booking.totalPrice}")
                }
            }
        }
    }
}
