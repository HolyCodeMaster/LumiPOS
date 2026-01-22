package com.project.lumipos.ui.components

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
import androidx.compose.ui.window.Dialog
import com.project.lumipos.model.*
import com.project.lumipos.ui.theme.LumiPOSColors

@Composable
fun BordInfoDialog(
    bord: Bord,
    ordre: Ordre?,
    onDismiss: () -> Unit,
    onVisOrdre: (String) -> Unit,
    onReserver: () -> Unit,
    onFrigør: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when {
                                        bord.erLedig -> LumiPOSColors.Grøn.copy(alpha = 0.15f)
                                        bord.erOptaget -> LumiPOSColors.Rød.copy(alpha = 0.15f)
                                        else -> LumiPOSColors.Orange.copy(alpha = 0.15f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            TableIcon(
                                size = 26.dp,
                                color = when {
                                    bord.erLedig -> LumiPOSColors.Grøn
                                    bord.erOptaget -> LumiPOSColors.Rød
                                    else -> LumiPOSColors.Orange
                                }
                            )
                        }
                        Column {
                            Text(
                                text = "Bord ${bord.nummer}",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                                color = LumiPOSColors.TekstLys,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = bord.status.displayNavn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = when {
                                    bord.erLedig -> LumiPOSColors.Grøn
                                    bord.erOptaget -> LumiPOSColors.Rød
                                    else -> LumiPOSColors.Orange
                                },
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    
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
                
                Divider(color = LumiPOSColors.SurfaceVariant)
                
                // Bord info
                when {
                    bord.erLedig -> {
                        // Ledigt bord
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "✓",
                                fontSize = 48.sp,
                                color = LumiPOSColors.Grøn
                            )
                            Text(
                                text = "Bordet er ledigt",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                color = LumiPOSColors.TekstLys,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Du kan tildele dette bord til en ny ordre",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = LumiPOSColors.TekstDæmpet
                            )
                        }
                    }
                    bord.erOptaget && ordre != null -> {
                        // Optaget bord med ordre
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
                                Text(
                                    text = "Aktiv Ordre",
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 14.sp),
                                    color = LumiPOSColors.TekstDæmpet,
                                    fontWeight = FontWeight.Medium
                                )
                                
                                InfoRække("Ordre ID", ordre.id)
                                InfoRække("Status", ordre.status.displayNavn, getStatusColor(ordre.status))
                                InfoRække("Total", "${ordre.total.toInt()} DKK", LumiPOSColors.Grøn)
                                InfoRække("Antal items", "${ordre.items.size}")
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Button(
                                    onClick = { onVisOrdre(ordre.id) },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.AccentBlå)
                                ) {
                                    Text("Se ordre detaljer", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                    else -> {
                        // Reserveret eller anden status
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "⏰",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "Bordet er reserveret",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                color = LumiPOSColors.TekstLys,
                                fontWeight = FontWeight.SemiBold
                            )
                            bord.reserveretTil?.let { navn ->
                                Text(
                                    text = "Reserveret til: $navn",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                    color = LumiPOSColors.TekstDæmpet
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Action buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when {
                        bord.erLedig -> {
                            Button(
                                onClick = onReserver,
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.Orange)
                            ) {
                                Text("⏰ Reserver bord", fontSize = 14.sp)
                            }
                        }
                        bord.erOptaget -> {
                            Button(
                                onClick = onFrigør,
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.Grøn)
                            ) {
                                Text("✓ Frigør bord", fontSize = 14.sp)
                            }
                        }
                        else -> {
                            Button(
                                onClick = onFrigør,
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.Rød)
                            ) {
                                Text("✕ Annuller reservation", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRække(
    label: String,
    værdi: String,
    værdiColor: Color = LumiPOSColors.TekstLys
) {
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
            text = værdi,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = værdiColor,
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
