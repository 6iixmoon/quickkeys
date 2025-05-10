package com.sam.quickkeys.ui.screens.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.AsyncImage
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.viewmodel.CarViewModel
import com.sam.quickkeys.repository.CarRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sam.quickkeys.data.CarDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun AdminScreen(
    carViewModel: CarViewModel = viewModel()
) {
    val cars by carViewModel.allCars.observeAsState(emptyList())

    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUrl = it.toString()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Admin Panel", style = MaterialTheme.typography.headlineLarge)

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
            onPickImage = { imagePickerLauncher.launch("image/*") },
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
    onPickImage: () -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    submitLabel: String
) {
    Spacer(modifier = Modifier.height(16.dp))
    Column {
        // Car Name Field
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Car Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Car Model Field
        OutlinedTextField(
            value = model,
            onValueChange = onModelChange,
            label = { Text("Model") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Car Type Field
        OutlinedTextField(
            value = type,
            onValueChange = onTypeChange,
            label = { Text("Type (e.g., SUV, Sedan)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Price Field
        OutlinedTextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text("Price Per Day") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Image Picker Field
        OutlinedTextField(
            value = imageUrl,
            onValueChange = onImageUrlChange,
            label = { Text("Car Image URL") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPickImage() },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            ),
            trailingIcon = {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Pick Image")
            }
        )

        if (imageUrl.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = imageUrl,
                contentDescription = "Selected Car Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Description Field
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(submitLabel, color = Color.White)
        }
    }
}

private fun TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
): TextFieldColors {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    val fakeCarDao = object : CarDao {
        override fun getAllCars(): LiveData<List<Car>> {
            val cars = listOf(
                Car(
                    id = 1,
                    name = "Toyota Camry",
                    model = "2022",
                    type = "Sedan",
                    pricePerDay = 50.0,
                    isAvailable = true,
                    description = "Reliable and fuel-efficient.",
                    imageUrl = "https://cdn.pixabay.com/photo/2012/05/29/00/43/car-49278_1280.jpg"
                ),
                Car(
                    id = 2,
                    name = "Tesla Model Y",
                    model = "2023",
                    type = "SUV",
                    pricePerDay = 120.0,
                    isAvailable = true,
                    description = "Electric and stylish.",
                    imageUrl = "https://cdn.pixabay.com/photo/2020/05/01/04/01/tesla-5114318_1280.jpg"
                )
            )
            return MutableLiveData(cars)
        }

        // Mock the other methods, or leave them empty
        override fun getCarsByModel(model: String): LiveData<List<Car>> = MutableLiveData(emptyList())
        override fun getCarsByType(type: String): LiveData<List<Car>> = MutableLiveData(emptyList())
        override fun getCarById(id: Int): Flow<Car> = flow { emit(Car(id, "Mock", "2023", "Sedan", 0.0, true, "Description", "")) }
        override fun getCarsSortedByPrice(): LiveData<List<Car>> = MutableLiveData(emptyList())
        override suspend fun insertCar(car: Car) {}
        override suspend fun updateCar(car: Car) {}
        override suspend fun deleteCar(car: Car) {}
    }

    val carRepository = CarRepository(fakeCarDao)
    val carViewModel = CarViewModel(carRepository)

    AdminScreen(carViewModel = carViewModel)
}
