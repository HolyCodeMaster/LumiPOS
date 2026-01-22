package com.project.lumipos.ui.components

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
import androidx.compose.ui.window.Dialog
import com.project.lumipos.model.*
import com.project.lumipos.ui.theme.LumiPOSColors

@Composable
fun OrdreDetaljerDialog(
    ordre: Ordre,
    onDismiss: () -> Unit,
    onMarkerSomBetalt: (String) -> Unit,
    onMarkerSomIGang: (String) -> Unit,
    onMarkerSomKlar: (String) -> Unit,
    onAnnuller: (String) -> Unit,
    onPrint: (Ordre) -> Unit
) {
    var visAnnullerKonfirmation by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(LumiPOSColors.AccentBlå.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            ListIcon(size = 22.dp, color = LumiPOSColors.AccentBlå)
                        }
                        Column {
                            Text(
                                text = "Ordre Detaljer",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                color = LumiPOSColors.TekstLys,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = ordre.id,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = LumiPOSColors.TekstDæmpet
                            )
                        }
                    }
                    
                    // Luk knap
                    IconButton(onClick = onDismiss) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(LumiPOSColors.SurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            CloseIcon(size = 18.dp, color = LumiPOSColors.TekstLys)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Ordre info
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InfoRow("Placering", ordre.bordEllerTakeaway)
                            InfoRow("Tidspunkt", "I dag 12:45")
                            InfoRow("Status", ordre.status.displayNavn, getStatusColor(ordre.status))
                        }
                    }
                    
                    // Produkter
                    Text(
                        text = "Produkter",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ordre.items.forEach { item ->
                                ProduktLinje(
                                    navn = item.produkt.navn,
                                    antal = item.antal,
                                    pris = item.produkt.pris,
                                    total = item.antal * item.produkt.pris
                                )
                            }
                        }
                    }
                    
                    // Total
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.AccentBlå.copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TotalRække("Subtotal", ordre.total.toInt() - ordre.moms.toInt())
                            TotalRække("Moms (25%)", ordre.moms.toInt())
                            Divider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = LumiPOSColors.TekstDæmpet.copy(alpha = 0.3f)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total",
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                    color = LumiPOSColors.TekstLys,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${ordre.total.toInt()} DKK",
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                    color = LumiPOSColors.AccentBlå,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons baseret på status
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (ordre.status) {
                        OrdreStatusType.VENTER_BETALING -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onMarkerSomBetalt(ordre.id) },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LumiPOSColors.Grøn
                                    )
                                ) {
                                    Text("✓ Marker som betalt", fontSize = 14.sp)
                                }
                                Button(
                                    onClick = { onMarkerSomIGang(ordre.id) },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LumiPOSColors.Orange
                                    )
                                ) {
                                    Text("◷ Marker som i gang", fontSize = 14.sp)
                                }
                            }
                        }
                        OrdreStatusType.I_GANG -> {
                            Button(
                                onClick = { onMarkerSomKlar(ordre.id) },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LumiPOSColors.Grøn
                                )
                            ) {
                                Text("✓ Marker som klar til betaling", fontSize = 14.sp)
                            }
                        }
                        OrdreStatusType.KLAR -> {
                            Button(
                                onClick = { onMarkerSomBetalt(ordre.id) },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LumiPOSColors.Grøn
                                )
                            ) {
                                Text("✓ Registrer betaling", fontSize = 14.sp)
                            }
                        }
                        else -> {}
                    }
                    
                    // Print og Annuller knapper
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onPrint(ordre) },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LumiPOSColors.AccentBlå
                            )
                        ) {
                            Text("🖨 Print", fontSize = 14.sp)
                        }
                        
                        if (ordre.status != OrdreStatusType.BETALT && ordre.status != OrdreStatusType.ANNULLERET) {
                            Button(
                                onClick = { visAnnullerKonfirmation = true },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LumiPOSColors.Rød.copy(alpha = 0.8f)
                                )
                            ) {
                                Text("✕ Annuller", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Annuller konfirmation dialog
    if (visAnnullerKonfirmation) {
        AlertDialog(
            onDismissRequest = { visAnnullerKonfirmation = false },
            title = {
                Text(
                    "Annuller ordre?",
                    color = LumiPOSColors.TekstLys
                )
            },
            text = {
                Text(
                    "Er du sikker på du vil annullere ordre ${ordre.id}? Dette kan ikke fortrydes.",
                    color = LumiPOSColors.TekstDæmpet
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAnnuller(ordre.id)
                        visAnnullerKonfirmation = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.Rød)
                ) {
                    Text("Ja, annuller")
                }
            },
            dismissButton = {
                TextButton(onClick = { visAnnullerKonfirmation = false }) {
                    Text("Nej", color = LumiPOSColors.TekstLys)
                }
            },
            containerColor = LumiPOSColors.Panel
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, valueColor: Color = LumiPOSColors.TekstLys) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = LumiPOSColors.TekstDæmpet
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ProduktLinje(navn: String, antal: Int, pris: Int, total: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(LumiPOSColors.AccentBlå)
            )
            Column {
                Text(
                    text = navn,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${antal}x ${pris} DKK",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = LumiPOSColors.TekstDæmpet
                )
            }
        }
        Text(
            text = "${total} DKK",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = LumiPOSColors.TekstLys,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TotalRække(label: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = LumiPOSColors.TekstDæmpet
        )
        Text(
            text = "${value} DKK",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = LumiPOSColors.TekstLys,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun getStatusColor(status: OrdreStatusType): Color {
    return when (status) {
        OrdreStatusType.VENTER_BETALING -> LumiPOSColors.AccentBlå
        OrdreStatusType.I_GANG -> LumiPOSColors.Orange
        OrdreStatusType.KLAR -> LumiPOSColors.Grøn
        OrdreStatusType.BETALT -> LumiPOSColors.Grøn
        OrdreStatusType.ANNULLERET -> LumiPOSColors.Rød
    }
}

