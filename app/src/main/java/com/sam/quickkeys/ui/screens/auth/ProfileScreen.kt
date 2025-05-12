package com.sam.quickkeys.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sam.quickkeys.R
import com.sam.quickkeys.navigation.ROUT_LOGIN
import com.sam.quickkeys.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState()

    // Redirect if user is not logged in
    if (currentUser == null) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Please log in.", Toast.LENGTH_SHORT).show()
            navController.navigate(ROUT_LOGIN) {
                popUpTo(ROUT_LOGIN) { inclusive = true }
            }
        }
        return
    }

    val user = currentUser!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickKeys", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logoutUser()
                        Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate(ROUT_LOGIN) {
                            popUpTo(ROUT_LOGIN) { inclusive = true }
                        }
                    }) {
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
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

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


            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = "Back to Home")
            }

            Button(
                onClick = {
                    authViewModel.logoutUser()
                    Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show()
                    navController.navigate(ROUT_LOGIN) {
                        popUpTo(ROUT_LOGIN) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Logout")
            }



        }
    }
}
