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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.sam.quickkeys.data.AppDatabase
import com.sam.quickkeys.repository.CarRepository
import com.sam.quickkeys.viewmodel.CarViewModel
import com.sam.quickkeys.viewmodel.CarViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(carId: Int, navController: NavHostController) {
    val context = LocalContext.current

    val carViewModel: CarViewModel = viewModel(
        factory = CarViewModelFactory(
            CarRepository(AppDatabase.getDatabase(context).carDao())
        )
    )

    val car by carViewModel.getCarById(carId).observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = car?.name?.let { "$it Details" } ?: "Car Details",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            car?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(it.imageUrl),
                        contentDescription = "Car Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = "${it.name} ${it.model}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Type: ${it.type}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Price per Day: $${it.pricePerDay}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = if (it.isAvailable) "Available" else "Currently Unavailable",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (it.isAvailable) Color(0xFF388E3C) else Color.Red
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            navController.navigate("admin_with_car/${it.id}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = it.isAvailable,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Book Now", fontSize = 16.sp)
                    }
                }
            } ?: CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
