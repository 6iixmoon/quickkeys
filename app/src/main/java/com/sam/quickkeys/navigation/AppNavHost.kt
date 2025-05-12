package com.sam.quickkeys.navigation

import android.app.Application
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.sam.pay.ui.screens.about.AboutScreen
import com.sam.quickkeys.ui.screens.booking.BookingScreenWithFactory
import com.sam.quickkeys.ui.screens.home.HomeScreenContent
import com.sam.quickkeys.ui.screens.home.CarDetailsScreen
import com.sam.quickkeys.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {
    val context = LocalContext.current

    // Initialize ViewModels using appropriate factories
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            UserRepository(AppDatabase.getDatabase(context).userDao())
        )
    )

    // Get CarRepository instance
    val carRepository = CarRepository(AppDatabase.getDatabase(context).carDao())

    // Fetch all users when the app starts
    authViewModel.fetchAllUsers()

    // Observe the list of users using collectAsState for StateFlow
    val users = authViewModel.allUsers.collectAsState(initial = emptyList()).value

    // Define NavHost with all app destinations
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home screen showing a list of cars
        composable(ROUT_HOME) {
            val currentUser by authViewModel.currentUser.collectAsState()

            // Pass carViewModel to the HomeScreenContent
            HomeScreenContent(
                navController = navController,
                carViewModel = viewModel(factory = CarViewModelFactory(carRepository)), // Pass ViewModel here
                isAdmin = currentUser?.role == "admin" // ✅ Set this correctly based on user role
            )
        }

        // Admin screen to manage cars (CRUD operations)
        composable("admin") {
            val bookingViewModel: BookingViewModel = viewModel()
            AdminScreen(
                carRepository = carRepository,
                navController = navController,
                carViewModel = viewModel(factory = CarViewModelFactory(carRepository)),
                bookingViewModel = bookingViewModel
            )
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

        // Profile screen displaying current logged-in user details
        composable("profile") {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }

        // Booking screen for reserving a car
        composable("booking/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")?.toIntOrNull()

            if (carId != null) {
                val carDao = AppDatabase.getDatabase(context).carDao()
                val carRepository = CarRepository(carDao)

                val carViewModel: CarViewModel = viewModel(
                    factory = CarViewModelFactory(carRepository)
                )

                val car by carViewModel.getCarById(carId).observeAsState()

                car?.let {
                    BookingScreenWithFactory(
                        car = it,
                        userId = 123, // Or pass actual userId
                        navController = navController
                    )
                } ?: CircularProgressIndicator()
            }
        }


        // About screen with app information
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        // Splash screen displayed at the start
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }

        // Car details screen showing full car info
        composable(
            route = ROUT_CAR,
            arguments = listOf(navArgument("carId") { type = NavType.IntType })
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("carId") ?: 0
            val carViewModel: CarViewModel = viewModel(
                factory = CarViewModelFactory(carRepository)
            )
            val carLiveData = carViewModel.getCarById(carId)
            val car = carLiveData.observeAsState().value

            car?.let {
                CarDetailsScreen(carId = carId, navController = navController)
            }
        }
    }
}
