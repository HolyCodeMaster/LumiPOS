package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.project.lumipos.model.Ordre
import com.project.lumipos.ui.theme.LumiPOSColors

@Composable
fun OrdreListeDialog(
    titel: String,
    ordrer: List<Ordre>,
    accentFarve: androidx.compose.ui.graphics.Color,
    onDismiss: () -> Unit,
    onOrdreKlik: (String) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = titel,
                        style = MaterialTheme.typography.headlineSmall,
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        CloseIcon(size = 20.dp, color = LumiPOSColors.TekstLys)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "${ordrer.size} ordrer i alt",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LumiPOSColors.TekstDæmpet
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Ordre liste
                if (ordrer.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ingen ordrer at vise",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LumiPOSColors.TekstDæmpet
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(ordrer) { ordre ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                onClick = { 
                                    onOrdreKlik(ordre.id)
                                    onDismiss()
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(accentFarve)
                                        )
                                        Column {
                                            Text(
                                                text = ordre.bordEllerTakeaway,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = LumiPOSColors.TekstLys,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "#${ordre.id}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = LumiPOSColors.TekstDæmpet
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${ordre.total} DKK",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = accentFarve,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
