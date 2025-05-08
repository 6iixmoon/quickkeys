package com.sam.quickkeys.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.viewmodel.CarViewModel

@Composable
fun AdminScreen(carViewModel: CarViewModel = viewModel()) {
    val cars by carViewModel.allCars.observeAsState(emptyList())

    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Admin Panel", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        AdminCarForm(
            name = name,
            onNameChange = { name = it },
            model = model,
            onModelChange = { model = it },
            type = type,
            onTypeChange = { type = it },
            price = price,
            onPriceChange = { price = it },
            imageUrl = imageUrl,
            onImageUrlChange = { imageUrl = it },
            description = description,
            onDescriptionChange = { description = it },
            onSubmit = {
                val car = Car(
                    id = selectedCar?.id ?: 0,
                    name = name,
                    model = model,
                    type = type,
                    pricePerDay = price.toDoubleOrNull() ?: 0.0,
                    imageUrl = imageUrl,
                    isAvailable = true,
                    description = description
                )
                if (selectedCar == null) {
                    carViewModel.addCar(car)
                } else {
                    carViewModel.updateCar(car)
                }

                // Reset form
                name = ""
                model = ""
                type = ""
                price = ""
                imageUrl = ""
                description = ""
                selectedCar = null
            },
            submitLabel = if (selectedCar == null) "Add Car" else "Update Car"
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn {
            items(cars.size) { index ->
                val car = cars[index]
                AdminCarItem(
                    car = car,
                    onEdit = {
                        selectedCar = it
                        name = it.name
                        model = it.model
                        type = it.type
                        price = it.pricePerDay.toString()
                        imageUrl = it.imageUrl
                        description = it.description
                    },
                    onDelete = { carViewModel.deleteCar(it) }
                )
            }
        }
    }
}

@Composable
fun AdminCarForm(
    name: String,
    onNameChange: (String) -> Unit,
    model: String,
    onModelChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    imageUrl: String,
    onImageUrlChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    submitLabel: String
) {
    Column {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Car Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = model,
            onValueChange = onModelChange,
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = type,
            onValueChange = onTypeChange,
            label = { Text("Type (e.g., SUV, Sedan)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text("Price Per Day") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = onImageUrlChange,
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(submitLabel)
        }
    }
}
