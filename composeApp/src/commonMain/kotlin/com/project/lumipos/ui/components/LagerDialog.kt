package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.project.lumipos.ui.theme.LumiPOSColors

data class LagerVare(
    val id: String,
    val navn: String,
    val status: LagerStatus,
    val iconType: String
)

enum class LagerStatus(val displayNavn: String, val color: Color) {
    UDSOLGT("Udsolgt", Color(0xFFEF4444)),
    LAVT_LAGER("Lavt lager", Color(0xFFF97316)),
    PÅ_LAGER("På lager", Color(0xFF22C55E))
}

@Composable
fun LagerDialog(
    varer: List<LagerVare>,
    onDismiss: () -> Unit,
    onAdd: (navn: String, status: LagerStatus, iconType: String) -> Unit,
    onEdit: (LagerVare) -> Unit,
    onDelete: (String) -> Unit
) {
    var visTilføjDialog by remember { mutableStateOf(false) }
    var redigerVare by remember { mutableStateOf<LagerVare?>(null) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(600.dp)
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
                    Text(
                        text = "Administrer Lager",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Button(
                        onClick = { visTilføjDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPOSColors.Grøn
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("+ Tilføj vare")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Liste af varer
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    varer.forEach { vare ->
                        LagerVareCard(
                            vare = vare,
                            onEdit = { redigerVare = vare },
                            onDelete = { onDelete(vare.id) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Luk knap
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LumiPOSColors.SurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Luk", color = LumiPOSColors.TekstLys)
                }
            }
        }
    }
    
    // Tilføj/Rediger dialog
    if (visTilføjDialog || redigerVare != null) {
        TilføjRedigerVareDialog(
            eksisterendeVare = redigerVare,
            onDismiss = {
                visTilføjDialog = false
                redigerVare = null
            },
            onSave = { navn, status, iconType ->
                if (redigerVare != null) {
                    onEdit(redigerVare!!.copy(navn = navn, status = status, iconType = iconType))
                } else {
                    onAdd(navn, status, iconType)
                }
                visTilføjDialog = false
                redigerVare = null
            }
        )
    }
}

@Composable
fun LagerVareCard(
    vare: LagerVare,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                    when (vare.iconType) {
                        "coffee" -> CoffeeIcon(size = 24.dp, color = LumiPOSColors.TekstLys)
                        "drink" -> DrinkIcon(size = 24.dp, color = LumiPOSColors.TekstLys)
                        "bread" -> BreadIcon(size = 24.dp, color = LumiPOSColors.TekstLys)
                        else -> BoxIcon(size = 24.dp, color = LumiPOSColors.TekstLys)
                    }
                }
                Column {
                    Text(
                        text = vare.navn,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = vare.status.displayNavn,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = vare.status.color
                    )
                }
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onEdit) {
                    EditIcon(size = 18.dp, color = LumiPOSColors.AccentBlå)
                }
                IconButton(onClick = onDelete) {
                    DeleteIcon(size = 18.dp, color = LumiPOSColors.Rød)
                }
            }
        }
    }
}

@Composable
fun TilføjRedigerVareDialog(
    eksisterendeVare: LagerVare?,
    onDismiss: () -> Unit,
    onSave: (navn: String, status: LagerStatus, iconType: String) -> Unit
) {
    var navn by remember { mutableStateOf(eksisterendeVare?.navn ?: "") }
    var valgtStatus by remember { mutableStateOf(eksisterendeVare?.status ?: LagerStatus.PÅ_LAGER) }
    var valgtIkon by remember { mutableStateOf(eksisterendeVare?.iconType ?: "coffee") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.width(400.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (eksisterendeVare != null) "Rediger vare" else "Tilføj ny vare",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
                
                // Navn input
                OutlinedTextField(
                    value = navn,
                    onValueChange = { navn = it },
                    label = { Text("Varenavn") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = LumiPOSColors.TekstLys,
                        unfocusedTextColor = LumiPOSColors.TekstLys,
                        focusedBorderColor = LumiPOSColors.AccentBlå,
                        unfocusedBorderColor = LumiPOSColors.SurfaceVariant
                    )
                )
                
                // Status dropdown
                Column {
                    Text("Status:", color = LumiPOSColors.TekstDæmpet, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LagerStatus.entries.forEach { status ->
                            FilterChip(
                                selected = valgtStatus == status,
                                onClick = { valgtStatus = status },
                                label = { Text(status.displayNavn, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = status.color.copy(alpha = 0.2f),
                                    selectedLabelColor = status.color
                                )
                            )
                        }
                    }
                }
                
                // Ikon vælger
                Column {
                    Text("Ikon:", color = LumiPOSColors.TekstDæmpet, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf(
                            "coffee",
                            "drink",
                            "bread",
                            "food"
                        ).forEach { type ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = if (valgtIkon == type) 
                                            LumiPOSColors.AccentBlå.copy(alpha = 0.2f)
                                        else LumiPOSColors.SurfaceVariant,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { valgtIkon = type },
                                contentAlignment = Alignment.Center
                            ) {
                                when (type) {
                                    "coffee" -> CoffeeIcon(size = 24.dp, color = if (valgtIkon == type) LumiPOSColors.AccentBlå else LumiPOSColors.TekstDæmpet)
                                    "drink" -> DrinkIcon(size = 24.dp, color = if (valgtIkon == type) LumiPOSColors.AccentBlå else LumiPOSColors.TekstDæmpet)
                                    "bread" -> BreadIcon(size = 24.dp, color = if (valgtIkon == type) LumiPOSColors.AccentBlå else LumiPOSColors.TekstDæmpet)
                                    "food" -> BoxIcon(size = 24.dp, color = if (valgtIkon == type) LumiPOSColors.AccentBlå else LumiPOSColors.TekstDæmpet)
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Knapper
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPOSColors.SurfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Annuller", color = LumiPOSColors.TekstLys)
                    }
                    
                    Button(
                        onClick = { onSave(navn, valgtStatus, valgtIkon) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        enabled = navn.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPOSColors.Grøn
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Gem")
                    }
                }
            }
        }
    }
}
