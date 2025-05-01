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
import com.sam.quickkeys.data.UserDatabase
import com.sam.quickkeys.repository.UserRepository
import com.sam.quickkeys.ui.screens.RegisterScreen
import com.sam.quickkeys.ui.screens.auth.LoginScreen
import com.sam.quickkeys.viewmodel.AuthViewModel
import com.sam.quickkeys.viewmodel.AuthViewModelFactory

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_REGISTER
) {
    // ✅ Get context for Room database
    val context = LocalContext.current

    // ✅ Create database, repository, and ViewModelFactory
    val appDatabase = UserDatabase.getDatabase(context)
    val authRepository = UserRepository(appDatabase.userDao())
    val authViewModelFactory = AuthViewModelFactory(authRepository)

    // ✅ NavHost setup
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        // ✅ Register Screen
        composable(ROUT_REGISTER) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        // ✅ Login Screen
        composable(ROUT_LOGIN) {
            val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
            LoginScreen(
                onLogin = { username, password -> authViewModel.loginUser(username, password) },
                navController = navController,
                onSuccess = {
                    navController.navigate(ROUT_HOME) {
                        popUpTo(ROUT_LOGIN) { inclusive = true }
                    }
                }
            )
        }
    }
}