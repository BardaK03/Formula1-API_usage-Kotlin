package com.f1pulse.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverDetailScreen(
    driverId: String?,
    viewModel: DriverListViewModel,
    navController: NavController
) {
    val scrollState = rememberScrollState()

    // Get the current Firebase user ID
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUserId = firebaseAuth.currentUser?.uid ?: ""

    LaunchedEffect(driverId) {
        driverId?.let {
            viewModel.loadDriverDetails(it)
        }
    }

    val driver by viewModel.currentDriver.collectAsState()
    val error by viewModel.error.collectAsState()

    // Handle bookmark changes
    LaunchedEffect(error) {
        error?.let {
            // Show error if needed
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(driver?.let { "${it.givenName} ${it.familyName}" } ?: "Driver Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (driver == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${driver?.givenName} ${driver?.familyName}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(
                                    onClick = {
                                        driver?.let {
                                            if (currentUserId.isNotEmpty()) {
                                                viewModel.toggleBookmark(it.driverId, !it.isBookmarked, currentUserId)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (driver?.isBookmarked == true)
                                            Icons.Filled.Bookmark
                                        else
                                            Icons.Outlined.BookmarkBorder,
                                        contentDescription = if (driver?.isBookmarked == true)
                                            "Remove from bookmarks"
                                        else
                                            "Add to bookmarks"
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            DriverInfoItem(label = "Code", value = driver?.code)
                            DriverInfoItem(label = "Number", value = driver?.permanentNumber)
                            DriverInfoItem(label = "Date of Birth", value = driver?.dateOfBirth)
                            DriverInfoItem(label = "Nationality", value = driver?.nationality)

                            if (!driver?.url.isNullOrEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                Text(
                                    text = "More Info",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = driver?.url ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DriverInfoItem(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value ?: "N/A",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
