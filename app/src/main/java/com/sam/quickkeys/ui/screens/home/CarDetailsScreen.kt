package com.sam.quickkeys.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.sam.quickkeys.data.AppDatabase
import com.sam.quickkeys.repository.CarRepository
import com.sam.quickkeys.viewmodel.CarViewModel
import com.sam.quickkeys.viewmodel.CarViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(carId: Int) {
    val context = LocalContext.current

    // ViewModel with Factory
    val carViewModel: CarViewModel = viewModel(
        factory = CarViewModelFactory(
            CarRepository(AppDatabase.getDatabase(context).carDao())
        )
    )

    // Observe car LiveData
    val car by carViewModel.getCarById(carId).observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = car?.name?.let { "$it Details" } ?: "Car Details")
            })
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            car?.let {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {

                    Image(
                        painter = rememberAsyncImagePainter(it.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "${it.name} ${it.model}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Price/Day: $${it.pricePerDay}")
                    Text(text = "Description: ${it.description}")
                    Text(
                        text = if (it.isAvailable) "Available" else "Unavailable",
                        color = if (it.isAvailable) Color.Green else Color.Red
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // TODO: Add booking logic here
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = it.isAvailable
                    ) {
                        Text("Book Now")
                    }
                }
            } ?: CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
