package com.f1pulse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import com.f1pulse.ui.screens.DriverDetailScreen
import com.f1pulse.ui.screens.DriverListScreen
import com.f1pulse.ui.screens.DriverListViewModel
import com.f1pulse.ui.screens.HomeScreen
import com.f1pulse.ui.screens.CircuitsScreen
import com.f1pulse.ui.screens.BookmarksScreen
import com.f1pulse.ui.screens.SettingsScreen
import com.f1pulse.ui.screens.MainViewModel
import com.f1pulse.ui.theme.F1PulseTheme
import com.f1pulse.ui.screens.auth.WelcomeScreen
import com.f1pulse.ui.screens.auth.LoginScreen
import com.f1pulse.ui.screens.auth.RegisterScreen
import com.f1pulse.ui.screens.auth.AuthViewModel
import com.f1pulse.ui.screens.auth.AuthState
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.LaunchedEffect
import com.f1pulse.ui.screens.SettingsViewModel

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Explicitly initialize Firebase
        FirebaseApp.initializeApp(this)
        // Set Firebase Auth language code to English
        com.google.firebase.auth.FirebaseAuth.getInstance().setLanguageCode("en")

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val driverListViewModel: DriverListViewModel = viewModel()
            val mainViewModel: MainViewModel = viewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            val settingsViewModel: SettingsViewModel = hiltViewModel()

            val circuits by mainViewModel.circuits.collectAsState()
            val bookmarks by mainViewModel.bookmarks.collectAsState()
            val authState by authViewModel.authState.collectAsState()

            // Observe authentication state and navigate accordingly
            LaunchedEffect(authState) {
                when (authState) {
                    is AuthState.Authenticated -> {
                        if (navController.currentDestination?.route != "home") {
                            navController.navigate("home") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    is AuthState.Unauthenticated -> {
                        if (navController.currentDestination?.route != "login") {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    else -> {}
                }
            }

            F1PulseTheme(settingsViewModel = settingsViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                    ) {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("drivers") {
                            DriverListScreen(driverListViewModel, navController)
                        }
                        composable(
                            "driverDetail/{driverId}",
                            arguments = listOf(navArgument("driverId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val driverId = backStackEntry.arguments?.getString("driverId")
                            DriverDetailScreen(driverId, driverListViewModel, navController)
                        }
                        composable("circuits") {
                            CircuitsScreen(circuits, navController)
                        }
                        composable("bookmarks") {
                            val userId = (authState as? AuthState.Authenticated)?.let {
                                com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                            } ?: ""

                            // Load bookmarks for the current user when entering this screen
                            // Adding navController.currentBackStackEntry as a key to ensure refresh when navigating back
                            LaunchedEffect(userId, navController.currentBackStackEntry) {
                                if (userId.isNotEmpty()) {
                                    mainViewModel.getBookmarks(userId)
                                }
                            }

                            BookmarksScreen(
                                bookmarks,
                                onUnbookmark = { driver ->
                                    mainViewModel.unbookmark(driver, userId)
                                },
                                navController = navController
                            )
                        }
                        composable("settings") {
                            SettingsScreen(navController = navController)
                        }
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
                    }
                }
            }
        }
    }
}
