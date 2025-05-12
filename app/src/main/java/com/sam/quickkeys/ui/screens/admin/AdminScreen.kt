package com.sam.quickkeys.ui.screens.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.model.Booking
import com.sam.quickkeys.repository.CarRepository
import com.sam.quickkeys.viewmodel.CarViewModel
import com.sam.quickkeys.viewmodel.BookingViewModel
import com.sam.quickkeys.viewmodel.CarViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import android.util.Log
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    carRepository: CarRepository,
    navController: NavController,
    carViewModel: CarViewModel,
    bookingViewModel: BookingViewModel = viewModel()
) {
    val cars by carViewModel.allCars.observeAsState(emptyList())
    val bookings by bookingViewModel.bookings
    val snackbarHostState = remember { SnackbarHostState() }

    var name by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { imageUrl = it.toString() } }

    LaunchedEffect(Unit) {
        carViewModel.carActionMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
        bookingViewModel.fetchAllBookings()
        bookings.forEach {
            Log.d("AdminScreen", "Booking: $it")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickKeys", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
                snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Admin Panel", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("admin") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Back to Home")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AdminCarForm(
                name, { name = it },
                model, { model = it },
                type, { type = it },
                price, { price = it },
                imageUrl, { imageUrl = it },
                { imagePickerLauncher.launch("image/*") },
                description, { description = it },
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

            cars.forEach { car ->
                CarCard(
                    car = car,
                    onEdit = {
                        selectedCar = car
                        name = car.name
                        model = car.model
                        type = car.type
                        price = car.pricePerDay.toString()
                        imageUrl = car.imageUrl
                        description = car.description
                    },
                    onDelete = {
                        carViewModel.deleteCar(car)
                    }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("All Bookings", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            bookings.forEach { booking ->
                val relatedCar = cars.find { it.id == booking.carId }
                Log.d("AdminScreen", "Found car for booking: $relatedCar")
                BookingCard(booking = booking, car = relatedCar)
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking, car: Car?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("User ID: ${booking.userId}", style = MaterialTheme.typography.titleSmall)
            if (car != null) {
                Text("Car: ${car.name} ${car.model}")
            } else {
                Text("Car ID: ${booking.carId}")
            }
            Text("Start Date: ${booking.startDate}")
            Text("End Date: ${booking.endDate}")
            Text("Total Price: \$${booking.totalPrice}")
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
    Column {
        OutlinedTextField(value = name, onValueChange = onNameChange, label = { Text("Car Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = model, onValueChange = onModelChange, label = { Text("Model") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = type, onValueChange = onTypeChange, label = { Text("Type (SUV, Sedan, etc.)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = onPriceChange,
            label = { Text("Price Per Day") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Car Image", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))

        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { onPickImage() }
                    .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                    .padding(4.dp)
            ) {
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Selected Car Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tap to select image", color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
            Text(submitLabel, color = Color.White)
        }
    }
}

@Composable
fun CarCard(
    car: Car,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (car.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = car.imageUrl,
                    contentDescription = "Car Image",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 12.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(car.name, style = MaterialTheme.typography.titleMedium)
                Text("Model: ${car.model}")
                Text("Price: \$${car.pricePerDay}/day")
                Text("Type: ${car.type}")
            }

            IconButton(onClick = onEdit) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Car")
            }

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Car")
            }
        }
    }
}
