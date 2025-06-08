package com.f1pulse.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f1pulse.data.local.CircuitEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircuitsScreen(
    circuits: List<CircuitEntity>,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("F1 Circuits") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            if (circuits.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No circuits found")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        // In a real implementation, you would call refresh from MainViewModel
                        // mainViewModel.refreshCircuits()
                    }) {
                        Text("Refresh")
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(circuits) { circuit ->
                        CircuitCard(circuit)
                    }
                }
            }
        }
    }
}

@Composable
fun CircuitCard(circuit: CircuitEntity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = circuit.circuitName ?: "Unknown Circuit",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(
                    text = "Location: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${circuit.locality ?: "Unknown"}, ${circuit.country ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row {
                Text(
                    text = "Coordinates: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Lat: ${circuit.lat ?: "N/A"}, Long: ${circuit.longitude ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!circuit.url.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Wikipedia: ${circuit.url}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
