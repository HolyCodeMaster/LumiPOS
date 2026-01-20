package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.lumipos.model.NavigationTab
import com.project.lumipos.ui.theme.LumiPOSColors

@Composable
fun TopNavigationHeader(
    valgtTab: NavigationTab,
    onTabSkift: (NavigationTab) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = LumiPOSColors.Baggrund,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Øverste række: Logo + Info + Admin
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo og titel
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(LumiPOSColors.AccentBlå, LumiPOSColors.Lilla)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Text(
                        text = "LumiPOS",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Dato, tid og admin
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "📍", fontSize = 16.sp)
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "København, Danmark",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = LumiPOSColors.TekstDæmpet
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🕐", fontSize = 11.sp)
                                Text(
                                    text = "Tirsdag · 12:45",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                    color = LumiPOSColors.TekstLys,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Admin profil
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LumiPOSColors.SurfaceVariant)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👤", fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navigation tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NavigationTabItem(
                    tekst = "Oversigt",
                    ikon = "📊",
                    erValgt = valgtTab == NavigationTab.OVERSIGT,
                    onClick = { onTabSkift(NavigationTab.OVERSIGT) }
                )
                NavigationTabItem(
                    tekst = "Salg",
                    ikon = "🛒",
                    erValgt = valgtTab == NavigationTab.SALG,
                    onClick = { onTabSkift(NavigationTab.SALG) }
                )
            }
        }
    }
}

@Composable
fun NavigationTabItem(
    tekst: String,
    ikon: String,
    erValgt: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = ikon, fontSize = 18.sp)
            Text(
                text = tekst,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
                color = if (erValgt) LumiPOSColors.TekstLys else LumiPOSColors.TekstDæmpet,
                fontWeight = if (erValgt) FontWeight.Bold else FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(3.dp)
                .width(60.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (erValgt) LumiPOSColors.AccentBlå else Color.Transparent)
        )
    }
}
