package com.project.lumipos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.lumipos.data.DataSeeder
import com.project.lumipos.data.OrderRepository
import com.project.lumipos.model.NavigationTab
import com.project.lumipos.model.KurvItem
import com.project.lumipos.ui.theme.LumiPOSColors
import com.project.lumipos.ui.theme.lumiPOSDarkColorScheme
import com.project.lumipos.ui.components.TopNavigationHeader
import com.project.lumipos.ui.screens.OversigtScreen
import com.project.lumipos.ui.screens.SalgScreen
import com.project.lumipos.viewmodel.LumiViewModel

@Composable
@Preview
fun App() {
    // Initialize ViewModel og Repository
    val viewModel = remember { LumiViewModel() }
    
    // Seed data ved første opstart
    LaunchedEffect(Unit) {
        if (OrderRepository.instance.ordrer.value.isEmpty()) {
            DataSeeder.seed(OrderRepository.instance)
        }
    }
    
    MaterialTheme(colorScheme = lumiPOSDarkColorScheme()) {
        var valgtTab by remember { mutableStateOf(NavigationTab.OVERSIGT) }
        var kurv by remember { mutableStateOf(listOf<KurvItem>()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LumiPOSColors.Baggrund)
        ) {
            // Top navigation
            TopNavigationHeader(
                valgtTab = valgtTab,
                onTabSkift = { valgtTab = it }
            )

            // Main content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                when (valgtTab) {
                    NavigationTab.OVERSIGT -> OversigtScreen(viewModel = viewModel)
                    NavigationTab.SALG -> SalgScreen(
                        kurv = kurv,
                        onKurvOpdater = { kurv = it },
                        viewModel = viewModel
                    )
                    NavigationTab.HISTORIK -> com.project.lumipos.ui.screens.OrdreHistorikScreen(viewModel = viewModel)
                    NavigationTab.RAPPORTER -> com.project.lumipos.ui.screens.RapportScreen(viewModel = viewModel)
                }
            }
        }
    }
}
