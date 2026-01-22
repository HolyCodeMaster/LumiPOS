package com.project.lumipos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.lumipos.model.*
import com.project.lumipos.ui.theme.LumiPOSColors
import com.project.lumipos.viewmodel.LumiViewModel

@Composable
fun OrdreHistorikScreen(viewModel: LumiViewModel) {
    var søgeTekst by remember { mutableStateOf("") }
    var valgtFilter by remember { mutableStateOf("Alle") }
    var valgtOrdre by remember { mutableStateOf<Ordre?>(null) }
    var printOrdre by remember { mutableStateOf<Ordre?>(null) }
    
    // Collect alle ordrer
    val alleOrdrer by viewModel.allOrders.collectAsState()
    
    // Filtrerede ordrer baseret på søgning og filter
    val filtrerdeOrdrer = remember(alleOrdrer, søgeTekst, valgtFilter) {
        var ordrer = alleOrdrer
        
        // Filter efter status
        ordrer = when (valgtFilter) {
            "Venter" -> ordrer.filter { it.status == OrdreStatusType.VENTER_BETALING }
            "I gang" -> ordrer.filter { it.status == OrdreStatusType.I_GANG }
            "Klar" -> ordrer.filter { it.status == OrdreStatusType.KLAR }
            "Betalt" -> ordrer.filter { it.status == OrdreStatusType.BETALT }
            "Annulleret" -> ordrer.filter { it.status == OrdreStatusType.ANNULLERET }
            else -> ordrer
        }
        
        // Søg i ordre ID eller bord nummer
        if (søgeTekst.isNotBlank()) {
            ordrer = ordrer.filter {
                it.id.contains(søgeTekst, ignoreCase = true) ||
                it.bordEllerTakeaway.contains(søgeTekst, ignoreCase = true)
            }
        }
        
        // Sortér efter oprettelsestidspunkt (nyeste først)
        ordrer.sortedByDescending { it.oprettet }
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Ordre Historik",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${filtrerdeOrdrer.size} ordrer",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = LumiPOSColors.TekstDæmpet
                )
            }
        }
        
        // Søgebar og filtre
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Søgefelt
                OutlinedTextField(
                    value = søgeTekst,
                    onValueChange = { søgeTekst = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Søg efter ordre ID eller bord...",
                            color = LumiPOSColors.TekstDæmpet
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LumiPOSColors.AccentBlå,
                        unfocusedBorderColor = LumiPOSColors.SurfaceVariant,
                        focusedTextColor = LumiPOSColors.TekstLys,
                        unfocusedTextColor = LumiPOSColors.TekstLys
                    ),
                    singleLine = true
                )
                
                // Filter chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Alle", "Venter", "I gang", "Klar", "Betalt", "Annulleret").forEach { filter ->
                        HistorikFilterChip(
                            label = filter,
                            valgt = valgtFilter == filter,
                            onClick = { valgtFilter = filter }
                        )
                    }
                }
            }
        }
        
        // Ordre liste
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            if (filtrerdeOrdrer.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "📭",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "Ingen ordrer fundet",
                            style = MaterialTheme.typography.titleMedium,
                            color = LumiPOSColors.TekstDæmpet
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtrerdeOrdrer) { ordre ->
                        OrdreHistorikKort(
                            ordre = ordre,
                            onClick = { valgtOrdre = ordre }
                        )
                    }
                }
            }
        }
    }
    
    // Ordre detaljer dialog
    valgtOrdre?.let { ordre ->
        com.project.lumipos.ui.components.OrdreDetaljerDialog(
            ordre = ordre,
            onDismiss = { valgtOrdre = null },
            onMarkerSomBetalt = { ordreId ->
                viewModel.processPayment(ordreId)
                valgtOrdre = null
            },
            onMarkerSomIGang = { ordreId ->
                viewModel.startOrder(ordreId)
                valgtOrdre = null
            },
            onMarkerSomKlar = { ordreId ->
                viewModel.completeOrder(ordreId)
                valgtOrdre = null
            },
            onAnnuller = { ordreId ->
                viewModel.cancelOrder(ordreId)
            },
            onPrint = { ordre ->
                printOrdre = ordre
            }
        )
    }
    
    // Print dialog
    printOrdre?.let { ordre ->
        com.project.lumipos.ui.components.PrintKvitteringDialog(
            ordre = ordre,
            onDismiss = { printOrdre = null }
        )
    }
}

@Composable
private fun OrdreHistorikKort(
    ordre: Ordre,
    onClick: () -> Unit
) {
    val statusFarve = when (ordre.status) {
        OrdreStatusType.VENTER_BETALING -> LumiPOSColors.AccentBlå
        OrdreStatusType.I_GANG -> LumiPOSColors.Orange
        OrdreStatusType.KLAR -> LumiPOSColors.Grøn
        OrdreStatusType.BETALT -> LumiPOSColors.Grøn
        OrdreStatusType.ANNULLERET -> LumiPOSColors.Rød
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusFarve)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = ordre.id,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = ordre.bordEllerTakeaway,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                        Text(
                            text = ordre.status.displayNavn,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = statusFarve
                        )
                    }
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${ordre.total.toInt()} DKK",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "I dag 12:45", // Placeholder for timestamp
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = LumiPOSColors.TekstDæmpet
                )
            }
        }
    }
}

@Composable
private fun HistorikFilterChip(
    label: String,
    valgt: Boolean,
    onClick: () -> Unit
) {
    val baggrund = if (valgt) LumiPOSColors.AccentBlå.copy(alpha = 0.15f) else androidx.compose.ui.graphics.Color.Transparent
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
