package com.sam.quickkeys.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = car.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Model: ${car.model}")
                Text("Price per Day: $${car.pricePerDay}")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
