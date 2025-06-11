package com.f1pulse.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.f1pulse.ui.screens.auth.AuthState
import com.f1pulse.ui.screens.auth.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val isLoggedIn = authState is AuthState.Authenticated
    val username = if (authState is AuthState.Authenticated) {
        (authState as AuthState.Authenticated).displayName
    } else ""

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "F1Pulse",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            // Authentication Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoggedIn) {
                        Text(
                            text = "Welcome, $username",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { authViewModel.logout() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.ExitToApp,  // Using ExitToApp instead of Logout
                                contentDescription = "Logout",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Logout")
                        }
                    } else {
                        Text(
                            text = "Sign in to access all features",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = { navController.navigate("login") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Person,  // Using Person instead of Login
                                    contentDescription = "Login",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Login")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { navController.navigate("register") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.PersonAdd,
                                    contentDescription = "Register",
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Register")
                            }
                        }
                    }
                }
            }

            HomeMenuItem(
                title = "Drivers",
                icon = Icons.Default.Person,
                onClick = { navController.navigate("drivers") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeMenuItem(
                title = "Circuits",
                icon = Icons.Default.Place,
                onClick = { navController.navigate("circuits") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeMenuItem(
                title = "Bookmarks",
                icon = Icons.Default.Favorite,
                onClick = { navController.navigate("bookmarks") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            HomeMenuItem(
                title = "Settings",
                icon = Icons.Default.Settings,
                onClick = { navController.navigate("settings") }
            )
        }
    }
}

@Composable
fun HomeMenuItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate"
            )
        }
    }
}
