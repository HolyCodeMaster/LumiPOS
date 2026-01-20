package com.project.lumipos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.lumipos.model.*
import com.project.lumipos.ui.theme.LumiPOSColors
import com.project.lumipos.ui.components.StatusKortRække
import com.project.lumipos.ui.components.VentendeOgIGangPanel
import com.project.lumipos.ui.components.HøjreInfoPanel

@Composable
fun OversigtScreen() {
    var valgtFilter by remember { mutableStateOf("I dag") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OversigtHeader(
            valgtFilter = valgtFilter,
            onFilterSkift = { valgtFilter = it }
        )

        StatusKortRække()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            VentendeOgIGangPanel()
            HøjreInfoPanel()
        }
    }
}

@Composable
fun OversigtHeader(
    valgtFilter: String,
    onFilterSkift: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                com.project.lumipos.ui.components.MoneyIcon(size = 20.dp, color = LumiPOSColors.AccentBlå)
                Text(
                    text = "Samlet indtjening",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Oversigt over dagens aktivitet",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = LumiPOSColors.TekstDæmpet
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(label = "I dag", valgt = valgtFilter == "I dag", onClick = { onFilterSkift("I dag") })
            FilterChip(label = "Uge", valgt = valgtFilter == "Uge", onClick = { onFilterSkift("Uge") })
            FilterChip(label = "Måned", valgt = valgtFilter == "Måned", onClick = { onFilterSkift("Måned") })
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    valgt: Boolean,
    onClick: () -> Unit
) {
    val baggrund = if (valgt) LumiPOSColors.AccentBlå.copy(alpha = 0.15f) else Color.Transparent
    val kant = if (valgt) LumiPOSColors.AccentBlå else LumiPOSColors.SurfaceVariant
    val tekst = if (valgt) LumiPOSColors.AccentBlå else LumiPOSColors.TekstDæmpet

    Surface(
        modifier = Modifier.height(34.dp),
        shape = RoundedCornerShape(16.dp),
        color = baggrund,
        border = androidx.compose.foundation.BorderStroke(1.dp, kant),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = tekst,
                fontWeight = if (valgt) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
