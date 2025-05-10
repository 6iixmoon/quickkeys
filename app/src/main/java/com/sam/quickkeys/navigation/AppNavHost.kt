package com.sam.quickkeys.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sam.quickkeys.data.AppDatabase
import com.sam.quickkeys.repository.UserRepository
import com.sam.quickkeys.repository.CarRepository
import com.sam.quickkeys.ui.screens.RegisterScreen
import com.sam.quickkeys.ui.screens.admin.AdminScreen
import com.sam.quickkeys.ui.screens.auth.LoginScreen
import com.sam.quickkeys.ui.screens.auth.ProfileScreen
import com.sam.quickkeys.ui.screens.booking.BookingScreen
import com.sam.quickkeys.viewmodel.AuthViewModel
import com.sam.quickkeys.viewmodel.AuthViewModelFactory
import com.sam.quickkeys.viewmodel.CarViewModel
import com.sam.quickkeys.viewmodel.CarViewModelFactory
import com.sam.quickkeys.viewmodel.BookingViewModel
import androidx.compose.runtime.livedata.observeAsState
import com.sam.pay.ui.screens.about.AboutScreen
import com.sam.quickkeys.ui.screens.home.HomeScreenContent
import com.sam.quickkeys.model.Car
import com.sam.quickkeys.model.User
import com.sam.quickkeys.ui.screens.home.CarDetailsScreen
import com.sam.quickkeys.ui.screens.scaffold.ScaffoldScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SCAFFOLD
) {
    val context = LocalContext.current

    // Initialize ViewModels using appropriate factories
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            UserRepository(AppDatabase.getDatabase(context).userDao())
        )
    )

    val carViewModel: CarViewModel = viewModel(
        factory = CarViewModelFactory(
            CarRepository(AppDatabase.getDatabase(context).carDao())
        )
    )

    // Define NavHost with all app destinations
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home screen showing a list of cars
        composable(ROUT_HOME) {
            HomeScreenContent(navController, carViewModel)
        }

        // Register screen for user registration
        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        // Login screen for authentication
        composable(ROUT_LOGIN) {
            LoginScreen(authViewModel, navController) {
                navController.navigate(ROUT_HOME) {
                    popUpTo(ROUT_LOGIN) { inclusive = true }
                }
            }
        }

        // Admin screen to manage cars (CRUD operations)
        composable(ROUT_ADMIN) {
            AdminScreen(carViewModel = carViewModel)
        }

        // Profile screen displaying user details by userId
        composable(
            "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val user = User(id = userId, username = "John Doe", email = "johndoe@example.com", password = "password123", role = "Admin")

            ProfileScreen(navController = navController, user = user)
        }

        // Booking screen for reserving a car
        composable(
            route = "booking/{carId}/{userId}",
            arguments = listOf(
                navArgument("carId") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0

            val carLiveData = carViewModel.getCarById(carId)
            val car = carLiveData.observeAsState().value

            car?.let {
                BookingScreen(
                    car = it,
                    userId = userId,
                    bookingViewModel = BookingViewModel(context.applicationContext as Application),
                    navController = navController
                )
            }
        }

        // About screen with app information
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        // Scaffold screen managing the app layout (BottomBar, FAB, etc.)
        composable(ROUT_SCAFFOLD) {
            ScaffoldScreen(navController = navController)
        }

        // Car details screen showing full car info
        composable(
            route = ROUT_CAR,
            arguments = listOf(navArgument("carId") { type = NavType.IntType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            val carLiveData = carViewModel.getCarById(carId)
            val car = carLiveData.observeAsState().value

            car?.let {
                CarDetailsScreen(carId = carId, navController = navController)
            }
        }



    }
}

