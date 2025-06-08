package com.f1pulse

import android.os.Bundle
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
import com.f1pulse.ui.screens.DriverDetailScreen
import com.f1pulse.ui.screens.DriverListScreen
import com.f1pulse.ui.screens.DriverListViewModel
import com.f1pulse.ui.screens.HomeScreen
import com.f1pulse.ui.screens.CircuitsScreen
import com.f1pulse.ui.screens.BookmarksScreen
import com.f1pulse.ui.screens.SettingsScreen
import com.f1pulse.ui.screens.MainViewModel
import com.f1pulse.ui.theme.F1PulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val driverListViewModel: DriverListViewModel = viewModel()
            val mainViewModel: MainViewModel = viewModel()
            val circuits by mainViewModel.circuits.collectAsState()
            val bookmarks by mainViewModel.bookmarks.collectAsState()

            F1PulseTheme {
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
                            BookmarksScreen(bookmarks, onUnbookmark = { mainViewModel.unbookmark(it) }, navController = navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
