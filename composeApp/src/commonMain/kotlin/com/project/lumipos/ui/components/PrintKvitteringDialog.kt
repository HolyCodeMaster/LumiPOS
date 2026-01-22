package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.project.lumipos.model.Ordre
import com.project.lumipos.ui.theme.LumiPOSColors

@Composable
fun PrintKvitteringDialog(
    ordre: Ordre,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 700.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header med luk knap
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🖨 Kvittering",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        CloseIcon(size = 20.dp, color = Color.Black)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Kvittering preview (som kunne printes)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Restaurant info
                        Text(
                            text = "LumiPOS",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "København, Danmark",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "CVR: 12345678",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace
                        )
                        
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Black,
                            thickness = 2.dp
                        )
                        
                        // Ordre info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Ordre:",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                ordre.id,
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Placering:",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                ordre.bordEllerTakeaway,
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Dato:",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                "21/01/2026 12:45",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray
                        )
                        
                        // Produkter
                        ordre.items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "${item.antal}x ${item.produkt.navn}",
                                        fontSize = 12.sp,
                                        color = Color.Black,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        "@${item.produkt.pris} DKK",
                                        fontSize = 10.sp,
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Text(
                                    "${item.antal * item.produkt.pris} DKK",
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray
                        )
                        
                        // Totaler
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Subtotal:",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                "${(ordre.total - ordre.moms).toInt()} DKK",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Moms (25%):",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                "${ordre.moms.toInt()} DKK",
                                fontSize = 12.sp,
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.Black,
                            thickness = 2.dp
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "TOTAL:",
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                "${ordre.total.toInt()} DKK",
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Black,
                            thickness = 2.dp
                        )
                        
                        // Footer
                        Text(
                            text = "Tak for dit besøg!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Powered by LumiPOS",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            // I en reel app ville dette sende til printer
                            // For nu simulerer vi bare print
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPOSColors.AccentBlå
                        )
                    ) {
                        Text("🖨 Print", fontSize = 14.sp)
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPOSColors.SurfaceVariant
                        )
                    ) {
                        Text("Luk", fontSize = 14.sp, color = LumiPOSColors.TekstLys)
                    }
                }
            }
        }
    }
}
