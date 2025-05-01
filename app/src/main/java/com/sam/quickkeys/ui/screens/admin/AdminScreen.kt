package com.sam.quickkeys.ui.screens.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.viewmodel.CarViewModel

@Composable
fun AdminScreen(carViewModel: CarViewModel = viewModel()) {
    val cars by carViewModel.cars
    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Admin Panel", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Car Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price Per Day") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val car = Car(
                    id = selectedCar?.id ?: 0,
                    name = name,
                    model = model,
                    pricePerDay = price.toDoubleOrNull() ?: 0.0,
                    imageUrl = imageUrl,
                    isAvailable = true
                )

                if (selectedCar == null) {
                    carViewModel.addCar(car)
                } else {
                    carViewModel.updateCar(car)
                }

                // Reset form
                name = ""
                model = ""
                price = ""
                imageUrl = ""
                selectedCar = null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (selectedCar == null) "Add Car" else "Update Car")
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn {
            items(cars) { car ->
                AdminCarItem(
                    car = car,
                    onEdit = {
                        selectedCar = it
                        name = it.name
                        model = it.model
                        price = it.pricePerDay.toString()
                        imageUrl = it.imageUrl
                    },
                    onDelete = {
                        carViewModel.deleteCar(it)
                    }
                )
            }
        }
    }
}
