package com.sam.quickkeys.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.sam.quickkeys.R
import androidx.navigation.NavController
import com.sam.quickkeys.model.User
import com.sam.quickkeys.navigation.ROUT_LOGIN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: User // Receiving user object
) {
    val context = LocalContext.current
    var isLoggedIn by remember { mutableStateOf(true) }

    // Dummy logout logic (simply sets isLoggedIn to false)
    val logoutUser = {
        isLoggedIn = false
        Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show()
        navController.navigate(ROUT_LOGIN) {
            popUpTo(ROUT_LOGIN) { inclusive = true }
        }
    }

    if (!isLoggedIn) return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = logoutUser) {
                        Icon(painter = painterResource(id = R.drawable.lock), contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Profile Image (can be changed based on user data)
            Image(
                painter = painterResource(id = R.drawable.profile), // Placeholder profile image
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Info
            Text(
                text = user.username,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Text(
                text = "Role: ${user.role}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(onClick = logoutUser, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Logout")
            }
        }
    }
}
