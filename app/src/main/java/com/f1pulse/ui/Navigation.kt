package com.f1pulse.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.f1pulse.ui.screens.BookmarksScreen
import com.f1pulse.ui.screens.CircuitsScreen
import com.f1pulse.ui.screens.DriverDetailScreen
import com.f1pulse.ui.screens.DriverListScreen
import com.f1pulse.ui.screens.DriverListViewModel
import com.f1pulse.ui.screens.HomeScreen
import com.f1pulse.ui.screens.MainViewModel
import com.f1pulse.ui.screens.SettingsScreen
import com.f1pulse.ui.screens.SettingsViewModel
import com.f1pulse.ui.screens.auth.AuthState
import com.f1pulse.ui.screens.auth.AuthViewModel
import com.f1pulse.ui.screens.auth.LoginScreen
import com.f1pulse.ui.screens.auth.RegisterScreen
import com.f1pulse.ui.screens.auth.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.LaunchedEffect

@Composable
fun F1PulseNavGraph(
    navController: NavHostController,
    startDestination: String = "welcome"
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    // Get auth state and redirect if needed
    when (authState) {
        is AuthState.Authenticated -> {
            if (startDestination == "welcome") {
                navController.navigate("home") {
                    popUpTo("welcome") { inclusive = true }
                }
            }
        }
        else -> {
            // Let navigation proceed normally
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
    ) {
        // Auth screens
        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                onLogin = { email, password ->
                    authViewModel.login(email, password)
                }
            )
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                onRegister = { email, password, displayName ->
                    authViewModel.register(email, password, displayName)
                }
            )
        }

        // Main app screens
        composable("home") {
            HomeScreen(navController)
        }

        composable("drivers") {
            val driverListViewModel: DriverListViewModel = hiltViewModel()
            DriverListScreen(driverListViewModel, navController)
        }

        composable(
            "driverDetail/{driverId}",
            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
        ) { backStackEntry ->
            val driverId = backStackEntry.arguments?.getString("driverId")
            val driverListViewModel: DriverListViewModel = hiltViewModel()
            DriverDetailScreen(driverId, driverListViewModel, navController)
        }

        composable("circuits") {
            val mainViewModel: MainViewModel = hiltViewModel()
            val circuits by mainViewModel.circuits.collectAsState()
            CircuitsScreen(circuits, navController)
        }

        composable("bookmarks") {
            val mainViewModel: MainViewModel = hiltViewModel()
            val bookmarks by mainViewModel.bookmarks.collectAsState()
            val firebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser?.uid ?: ""

            // Load bookmarks for the current user when entering this screen
            LaunchedEffect(userId) {
                if (userId.isNotEmpty()) {
                    mainViewModel.getBookmarks(userId)
                }
            }

            BookmarksScreen(
                bookmarks = bookmarks,
                onUnbookmark = { driver -> mainViewModel.unbookmark(driver, userId) },
                navController = navController
            )
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}
