package com.project.lumipos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.lumipos.model.NavigationTab
import com.project.lumipos.model.KurvItem
import com.project.lumipos.ui.theme.LumiPOSColors
import com.project.lumipos.ui.theme.lumiPOSDarkColorScheme
import com.project.lumipos.ui.components.TopNavigationHeader
import com.project.lumipos.ui.screens.OversigtScreen
import com.project.lumipos.ui.screens.SalgScreen

@Composable
@Preview
fun App() {
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
                    NavigationTab.OVERSIGT -> OversigtScreen()
                    NavigationTab.SALG -> SalgScreen(
                        kurv = kurv,
                        onKurvOpdater = { kurv = it }
                    )
                }
            }
        }
    }
}
