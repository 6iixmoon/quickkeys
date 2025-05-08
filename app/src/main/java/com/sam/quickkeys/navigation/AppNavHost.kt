package com.sam.quickkeys.navigation

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
import com.sam.pay.ui.screens.about.AboutScreen
import com.sam.quickkeys.data.AppDatabase
import com.sam.quickkeys.repository.UserRepository
import com.sam.quickkeys.repository.CarRepository
import com.sam.quickkeys.ui.screens.RegisterScreen
import com.sam.quickkeys.ui.screens.about.HomeScreen
import com.sam.quickkeys.ui.screens.auth.LoginScreen
import com.sam.quickkeys.ui.screens.booking.BookingHistoryScreen
import com.sam.quickkeys.ui.screens.booking.BookingScreen
import com.sam.quickkeys.viewmodel.AuthViewModel
import com.sam.quickkeys.viewmodel.AuthViewModelFactory
import com.sam.quickkeys.viewmodel.CarViewModel
import com.sam.quickkeys.viewmodel.CarViewModelFactory

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_REGISTER
) {
    val context = LocalContext.current

    // Initialize AuthViewModel using factory
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            UserRepository(AppDatabase.getDatabase(context).userDao())
        )
    )

    // Initialize CarViewModel to handle car data
    val carViewModel: CarViewModel = viewModel(
        factory = CarViewModelFactory(
            CarRepository(AppDatabase.getDatabase(context).carDao())
        )
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home screen (car listings)
        composable(ROUT_HOME) {
            HomeScreen(navController, carViewModel) // Passing CarViewModel to HomeScreen
        }

        // About screen
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        // Authentication
        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        composable(ROUT_LOGIN) {
            LoginScreen(authViewModel, navController) {
                navController.navigate(ROUT_HOME) {
                    popUpTo(ROUT_LOGIN) { inclusive = true }
                }
            }
        }

        // Booking screen with car data passed as argument


        // Booking History screen
        composable(ROUT_HISTORY) {
            BookingHistoryScreen(userId = 1)
        }
    }
}
