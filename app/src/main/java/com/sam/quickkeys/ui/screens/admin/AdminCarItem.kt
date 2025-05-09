package com.sam.quickkeys.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sam.quickkeys.model.Car

@Composable
fun AdminCarItem(
    car: Car,
    onEdit: (Car) -> Unit,
    onDelete: (Car) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = car.imageUrl,
                contentDescription = "${car.name} Image",
                modifier = Modifier
                    .size(100.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
            ) {
                Text(
                    text = car.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Model: ${car.model}")
                Text("Price per Day: Ksh.${car.pricePerDay}")
            }

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { onEdit(car) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Car")
                }
                IconButton(onClick = { onDelete(car) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Car")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminCarItemPreview() {
    val sampleCar = Car(
        id = 1,
        type = "SUV",
        name = "Toyota RAV4",
        model = "2022",
        pricePerDay = 85.0,
        isAvailable = true,
        description = "A comfortable SUV with great mileage.",
        imageUrl = "https://cdn.pixabay.com/photo/2012/05/29/00/43/car-49278_1280.jpg"
    )

    AdminCarItem(
        car = sampleCar,
        onEdit = {},
        onDelete = {}
    )
}
