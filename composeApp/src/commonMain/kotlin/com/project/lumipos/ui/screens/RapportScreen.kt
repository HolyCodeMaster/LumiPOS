package com.project.lumipos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.project.lumipos.ui.components.ChartIcon
import com.project.lumipos.ui.components.LockIcon
import com.project.lumipos.ui.theme.LumiPOSColors
import com.project.lumipos.viewmodel.LumiViewModel

@Composable
fun RapportScreen(viewModel: LumiViewModel) {
    var valgtTab by remember { mutableStateOf("X-Rapport") }
    var visPinDialog by remember { mutableStateOf(false) }
    var visSuccesDialog by remember { mutableStateOf(false) }
    var fejlMelding by remember { mutableStateOf("") }
    
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
                    text = "Rapporter",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Dagens omsætning og statistik",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = LumiPOSColors.TekstDæmpet
                )
            }
        }
        
        // Tab selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RapportTab(
                    label = "X-Rapport",
                    ikon = { ChartIcon(size = 32.dp, color = if (valgtTab == "X-Rapport") LumiPOSColors.AccentBlå else LumiPOSColors.TekstLys) },
                    beskrivelse = "Mellemregning",
                    valgt = valgtTab == "X-Rapport",
                    onClick = { valgtTab = "X-Rapport" }
                )
                RapportTab(
                    label = "Z-Rapport",
                    ikon = { LockIcon(size = 32.dp, color = if (valgtTab == "Z-Rapport") LumiPOSColors.AccentBlå else LumiPOSColors.TekstLys) },
                    beskrivelse = "Dagsafslutning",
                    valgt = valgtTab == "Z-Rapport",
                    onClick = { valgtTab = "Z-Rapport" }
                )
            }
        }
        
        // Content baseret på valgt tab
        when (valgtTab) {
            "X-Rapport" -> XRapportPanel(viewModel = viewModel)
            "Z-Rapport" -> ZRapportPanel(
                viewModel = viewModel,
                onGenererRapport = { visPinDialog = true }
            )
        }
    }
    
    // PIN Dialog for Z-Rapport
    if (visPinDialog) {
        PinDialog(
            onDismiss = { visPinDialog = false },
            onConfirm = { pin ->
                val result = viewModel.generateZRapport(pin)
                result.onSuccess {
                    visPinDialog = false
                    visSuccesDialog = true
                    fejlMelding = ""
                }.onFailure { error ->
                    fejlMelding = error.message ?: "Ukendt fejl"
                }
            },
            fejlMelding = fejlMelding
        )
    }
    
    // Success dialog for Z-Rapport
    if (visSuccesDialog) {
        AlertDialog(
            onDismissRequest = { visSuccesDialog = false },
            title = {
                Text(
                    "✓ Z-Rapport Genereret",
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Dagens rapport er gemt i historikken. Tællere er nulstillet.",
                    color = LumiPOSColors.TekstDæmpet
                )
            },
            confirmButton = {
                Button(
                    onClick = { visSuccesDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.Grøn)
                ) {
                    Text("OK")
                }
            },
            containerColor = LumiPOSColors.Panel
        )
    }
}

@Composable
private fun RowScope.RapportTab(
    label: String,
    ikon: @Composable () -> Unit,
    beskrivelse: String,
    valgt: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (valgt) LumiPOSColors.AccentBlå.copy(alpha = 0.15f) 
            else LumiPOSColors.SurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (valgt) 2.dp else 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ikon()
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                color = if (valgt) LumiPOSColors.AccentBlå else LumiPOSColors.TekstLys,
                fontWeight = if (valgt) FontWeight.Bold else FontWeight.SemiBold
            )
            Text(
                text = beskrivelse,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = LumiPOSColors.TekstDæmpet
            )
        }
    }
}

@Composable
private fun ColumnScope.XRapportPanel(viewModel: LumiViewModel) {
    val xRapport = remember { viewModel.generateXRapport() }
    val dagensStatistik by viewModel.dagensStatistik.collectAsState()
    val ordrerVenter by viewModel.ordrerVenterBetaling.collectAsState()
    val ordrerIGang by viewModel.ordrerIGang.collectAsState()
    val ledigeBorde by viewModel.ledigeBorde.collectAsState()
    val optagedeBorde by viewModel.optagedeBorde.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Omsætning kort
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(LumiPOSColors.Grøn.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        com.project.lumipos.ui.components.MoneyIcon(size = 22.dp, color = LumiPOSColors.Grøn)
                    }
                    Column {
                        Text(
                            text = "Dagens Omsætning",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            color = LumiPOSColors.TekstLys,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "21. Januar 2026",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                    }
                }
                
                Divider(color = LumiPOSColors.SurfaceVariant)
                
                StatistikRække("Antal ordrer", "${xRapport.statistik.antalOrdrer}")
                StatistikRække("Total omsætning", "${xRapport.statistik.omsætning} DKK", LumiPOSColors.Grøn)
                StatistikRække("Heraf moms (25%)", "${xRapport.statistik.moms} DKK")
                StatistikRække("Netto omsætning", "${xRapport.statistik.netto} DKK", LumiPOSColors.AccentBlå)
            }
        }
        
        // Top produkter
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🏆", fontSize = 24.sp)
                    Text(
                        text = "Top 5 Produkter",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Divider(color = LumiPOSColors.SurfaceVariant)
                
                xRapport.statistik.topProdukter.forEachIndexed { index, (produkt, antal) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(LumiPOSColors.AccentBlå.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                    color = LumiPOSColors.AccentBlå,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = produkt,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                color = LumiPOSColors.TekstLys
                            )
                        }
                        Text(
                            text = "$antal stk",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            color = LumiPOSColors.TekstDæmpet,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        
        // Bord og ordre status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bord status
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🏠 Bord Status",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.SemiBold
                    )
                    StatistikRække("Ledige", "${ledigeBorde.size}", LumiPOSColors.Grøn)
                    StatistikRække("Optagne", "${optagedeBorde.size}", LumiPOSColors.Orange)
                }
            }
            
            // Aktive ordrer
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "📋 Aktive Ordrer",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.SemiBold
                    )
                    StatistikRække("Venter", "${ordrerVenter.size}", LumiPOSColors.AccentBlå)
                    StatistikRække("I gang", "${ordrerIGang.size}", LumiPOSColors.Orange)
                }
            }
        }
        
        // Eksporter knap
        Button(
            onClick = {
                val csvContent = com.project.lumipos.utils.CsvExporter.generateXRapportCsv(xRapport)
                val filename = com.project.lumipos.utils.CsvExporter.generateXRapportFilename()
                com.project.lumipos.utils.downloadCsv(csvContent, filename)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.AccentBlå)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📥", fontSize = 18.sp)
                Text("Eksporter X-Rapport til CSV", fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ColumnScope.ZRapportPanel(
    viewModel: LumiViewModel,
    onGenererRapport: () -> Unit
) {
    val xRapport = remember { viewModel.generateXRapport() }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Advarsel kort
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Orange.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⚠️", fontSize = 32.sp)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Dagsafslutning",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Z-rapporten gemmer dagens data permanent og nulstiller tællere. Kan kun udføres én gang per dag og kræver admin PIN.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = LumiPOSColors.TekstDæmpet
                    )
                }
            }
        }
        
        // Preview af dagens data
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
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
                Text(
                    text = "Dagens data der vil blive gemt:",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.SemiBold
                )
                
                Divider(color = LumiPOSColors.SurfaceVariant)
                
                StatistikRække("Antal ordrer", "${xRapport.statistik.antalOrdrer}")
                StatistikRække("Total omsætning", "${xRapport.statistik.omsætning} DKK", LumiPOSColors.Grøn)
                StatistikRække("Heraf moms", "${xRapport.statistik.moms} DKK")
                StatistikRække("Netto omsætning", "${xRapport.statistik.netto} DKK", LumiPOSColors.AccentBlå)
            }
        }
        
        // Generer Z-rapport knap
        Button(
            onClick = onGenererRapport,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.Rød)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔒", fontSize = 20.sp)
                Text("Generer Z-Rapport", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun StatistikRække(
    label: String,
    værdi: String,
    værdiColor: Color = LumiPOSColors.TekstLys
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = LumiPOSColors.TekstDæmpet
        )
        Text(
            text = værdi,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = værdiColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PinDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    fejlMelding: String
) {
    var pin by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🔐", fontSize = 32.sp)
                Text(
                    "Admin PIN Påkrævet",
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Indtast admin PIN for at generere Z-rapport",
                    color = LumiPOSColors.TekstDæmpet,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    placeholder = { Text("1234") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LumiPOSColors.AccentBlå,
                        unfocusedBorderColor = LumiPOSColors.SurfaceVariant,
                        focusedTextColor = LumiPOSColors.TekstLys,
                        unfocusedTextColor = LumiPOSColors.TekstLys
                    ),
                    singleLine = true
                )
                
                if (fejlMelding.isNotEmpty()) {
                    Text(
                        text = "❌ $fejlMelding",
                        color = LumiPOSColors.Rød,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Text(
                    text = "Standard PIN: 1234",
                    color = LumiPOSColors.TekstDæmpet.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(pin) },
                enabled = pin.length == 4,
                colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.AccentBlå)
            ) {
                Text("Bekræft")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuller", color = LumiPOSColors.TekstLys)
            }
        },
        containerColor = LumiPOSColors.Panel
    )
}
