package com.sam.quickkeys.ui.screens.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sam.quickkeys.model.User
import com.sam.quickkeys.ui.screens.auth.ProfileScreen
import com.sam.quickkeys.ui.screens.home.HomeScreenContent
import com.sam.quickkeys.viewmodel.AuthViewModel

@Composable
fun ScaffoldScreen(
    userViewModel: AuthViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    var selectedIndex by remember { mutableStateOf(0) }

    // Ideally get user from ViewModel (replace this with real logic)
    val user = User(
        id = 1,
        username = "John Doe",
        email = "johndoe@example.com",
        password = "password123",
        role = "Admin"
    )

    Scaffold(
        floatingActionButton = {
            if (user.role == "Admin") {
                FloatingActionButton(
                    onClick = { navController.navigate("admin") },
                    containerColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Gray,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedIndex) {
                0 -> HomeScreenContent(navController = navController)
                1 -> ProfileScreen(navController = navController, user = user)
            }
        }
    }
}
