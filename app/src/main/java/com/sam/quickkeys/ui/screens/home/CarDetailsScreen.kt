package com.sam.quickkeys.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sam.quickkeys.model.Car

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(carId: Int) {
    // Retrieve car details by ID from the ViewModel or Repository
    val car = getCarById(carId)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("${car.name} Details") })
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = rememberAsyncImagePainter(car.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${car.name} ${car.model}", style = MaterialTheme.typography.titleLarge)
            Text(text = "Price/Day: $${car.pricePerDay}")
            Text(text = "Description: $${car.description}")
            Text(
                text = if (car.isAvailable) "Available" else "Unavailable",
                color = if (car.isAvailable) Color.Green else Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Add booking logic here */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Book Now")
            }
        }
    }
}

// Dummy function to simulate fetching car details by ID
fun getCarById(carId: Int): Car {
    // Replace this with actual logic to fetch car by ID from a repository or ViewModel
    return Car(
        id = carId,
        name = "Car $carId",
        model = "Model $carId",
        pricePerDay = 100.0,  // Make sure this is a Double (not Int)
        isAvailable = true,
        description = "Detailed description of car.",
        imageUrl = "https://example.com/car_image.jpg"
    )

}
