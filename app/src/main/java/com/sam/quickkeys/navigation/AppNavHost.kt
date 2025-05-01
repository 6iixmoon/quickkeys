package com.sam.quickkeys.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sam.pay.ui.screens.about.AboutScreen
import com.sam.pay.ui.screens.about.HomeScreen
import com.sam.quickkeys.data.AppDatabase
import com.sam.quickkeys.repository.UserRepository
import com.sam.quickkeys.ui.screens.RegisterScreen
import com.sam.quickkeys.ui.screens.auth.LoginScreen
import com.sam.quickkeys.ui.screens.booking.BookingHistoryScreen
import com.sam.quickkeys.ui.screens.booking.BookingScreen
import com.sam.quickkeys.viewmodel.AuthViewModel
import com.sam.quickkeys.viewmodel.AuthViewModelFactory

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

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home screen (car listings)
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }

        // About screen
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        // Register screen
        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        // Login screen
        composable(ROUT_LOGIN) {
            LoginScreen(authViewModel, navController) {
                navController.navigate(ROUT_HOME) {
                    popUpTo(ROUT_LOGIN) { inclusive = true }
                }
            }
        }
        composable(ROUT_BOOKING) {
            val car = // pass via nav args or shared ViewModel
                BookingScreen(car = car, userId = 1, navController = navController)
        }

        composable(ROUT_HISTORY) {
            BookingHistoryScreen(userId = 1)
        }

    }
}
