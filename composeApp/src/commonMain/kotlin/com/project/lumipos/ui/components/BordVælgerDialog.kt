package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.project.lumipos.model.Bord
import com.project.lumipos.model.BordStatusType
import com.project.lumipos.ui.theme.LumiPOSColors

/**
 * Bord-vælger dialog
 * Vises når brugeren klikker "Betal" i kurven
 */
@Composable
fun BordVælgerDialog(
    borde: List<Bord>,
    onDismiss: () -> Unit,
    onBordValgt: (Int) -> Unit,
    onTakeaway: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Text(
                    text = "Vælg bord eller takeaway",
                    style = MaterialTheme.typography.headlineSmall,
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Vælg et ledigt bord til ordren",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LumiPOSColors.TekstDæmpet
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Takeaway knap
                Button(
                    onClick = onTakeaway,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LumiPOSColors.Orange)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BoxIcon(size = 20.dp, color = Color.White)
                        Text(
                            text = "Takeaway",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Bord grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(borde) { bord ->
                        BordItem(
                            bord = bord,
                            onClick = {
                                if (bord.erLedig) {
                                    onBordValgt(bord.nummer)
                                }
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LegendItem(
                        color = BordStatusType.LEDIG.color,
                        label = "Ledig"
                    )
                    LegendItem(
                        color = BordStatusType.OPTAGET.color,
                        label = "Optaget"
                    )
                    LegendItem(
                        color = BordStatusType.RESERVERET.color,
                        label = "Reserveret"
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Annuller knap
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = LumiPOSColors.TekstDæmpet
                    )
                ) {
                    Text(
                        text = "Annuller",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BordItem(
    bord: Bord,
    onClick: () -> Unit
) {
    val backgroundColor = when (bord.status) {
        BordStatusType.LEDIG -> bord.status.color.copy(alpha = 0.15f)
        BordStatusType.OPTAGET -> bord.status.color.copy(alpha = 0.15f)
        BordStatusType.RESERVERET -> bord.status.color.copy(alpha = 0.15f)
    }
    
    val borderColor = bord.status.color
    val isClickable = bord.erLedig
    
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (isClickable) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TableIcon(size = 24.dp, color = borderColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${bord.nummer}",
                style = MaterialTheme.typography.titleMedium,
                color = borderColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = bord.status.displayNavn,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = borderColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = LumiPOSColors.TekstDæmpet,
            fontSize = 12.sp
        )
    }
}
