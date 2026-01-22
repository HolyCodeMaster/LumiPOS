package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.project.lumipos.model.OrdreStatus
import com.project.lumipos.ui.theme.LumiPOSColors

@Composable
fun StatusKortRække(
    ordrerVenter: List<com.project.lumipos.model.Ordre>,
    ordrerIGang: List<com.project.lumipos.model.Ordre>,
    ordrerBetalt: Int,
    onKortKlik: (String) -> Unit = {}
) {
    val totalVenter = ordrerVenter.sumOf { it.total }
    val totalIGang = ordrerIGang.sumOf { it.total }
    
    val kort = listOf(
        OrdreStatus("Venter på betaling", ordrerVenter.size, "DKK $totalVenter", LumiPOSColors.AccentBlå, "clock", 0.65f),
        OrdreStatus("I gang", ordrerIGang.size, "DKK $totalIGang", LumiPOSColors.Orange, "progress", 0.45f),
        OrdreStatus("Afsluttede ordrer", ordrerBetalt, "DKK 0", LumiPOSColors.Grøn, "check", 0.85f)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        kort.forEachIndexed { index, status ->
            val statusType = when(index) {
                0 -> "VENTER"
                1 -> "I_GANG"
                else -> "BETALT"
            }
            StatusKort(
                status = status, 
                modifier = Modifier.weight(1f),
                onClick = { onKortKlik(statusType) }
            )
        }
    }
}

@Composable
fun RowScope.StatusKort(
    status: OrdreStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(status.accent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    when (status.ikon) {
                        "clock" -> ClockIcon(size = 22.dp, color = status.accent)
                        "progress" -> ProgressCircleIcon(size = 22.dp, color = status.accent)
                        "check" -> CheckIcon(size = 22.dp, color = status.accent)
                        else -> Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(status.accent))
                    }
                }
                Text(
                    text = status.beløb,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = status.accent,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column {
                Text(
                    text = status.titel,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = LumiPOSColors.TekstDæmpet
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${status.antal} ordrer",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgressBar(
                    progress = status.progress,
                    color = status.accent,
                    modifier = Modifier.height(4.dp)
                )
            }
        }
    }
}

@Composable
fun ProgressBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(LumiPOSColors.SurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
    }
}

@Composable
fun RowScope.VentendeOgIGangPanel(
    ordrerVenter: List<com.project.lumipos.model.Ordre>,
    ordrerIGang: List<com.project.lumipos.model.Ordre>,
    onOrderClick: (String) -> Unit,
    onHeaderClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .weight(1.5f)
            .fillMaxWidth()
            .heightIn(min = 500.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onHeaderClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(LumiPOSColors.AccentBlå.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        ListIcon(size = 18.dp, color = LumiPOSColors.AccentBlå)
                    }
                    Text(
                        text = "Ordrestatus",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                        color = LumiPOSColors.TekstLys,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${ordrerVenter.size + ordrerIGang.size} aktive",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = LumiPOSColors.TekstDæmpet
                    )
                    Text(
                        text = "→",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LumiPOSColors.AccentBlå
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 400.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OrdreKolonneListe(
                    titel = "Venter på betaling",
                    ikonType = "clock",
                    ordrer = ordrerVenter.take(5),
                    accentFarve = LumiPOSColors.AccentBlå,
                    onOrderClick = onOrderClick,
                    showMore = ordrerVenter.size > 5,
                    onShowMore = onHeaderClick
                )

                OrdreKolonneListe(
                    titel = "I gang",
                    ikonType = "progress",
                    ordrer = ordrerIGang.take(5),
                    accentFarve = LumiPOSColors.Orange,
                    onOrderClick = onOrderClick,
                    showMore = ordrerIGang.size > 5,
                    onShowMore = onHeaderClick
                )
            }
        }
    }
}

@Composable
fun RowScope.OrdreKolonneListe(
    titel: String,
    ikonType: String,
    ordrer: List<com.project.lumipos.model.Ordre>,
    accentFarve: Color,
    onOrderClick: (String) -> Unit,
    showMore: Boolean = false,
    onShowMore: () -> Unit = {}
) {
    Column(modifier = Modifier.weight(1f)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                when (ikonType) {
                    "clock" -> ClockIcon(size = 14.dp, color = accentFarve)
                    "progress" -> ProgressCircleIcon(size = 14.dp, color = accentFarve)
                    "check" -> CheckIcon(size = 14.dp, color = accentFarve)
                    else -> Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accentFarve))
                }
                Text(
                    text = titel,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = LumiPOSColors.TekstDæmpet,
                    fontWeight = FontWeight.Medium
                )
            }
            if (showMore) {
                Text(
                    text = "Se alle",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = accentFarve,
                    modifier = Modifier.clickable(onClick = onShowMore)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Scrollable list of clickable orders  
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 350.dp, max = 400.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (ordrer.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ingen ordrer",
                        style = MaterialTheme.typography.bodySmall,
                        color = LumiPOSColors.TekstDæmpet
                    )
                }
            } else {
                ordrer.forEach { ordre ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    onClick = { onOrderClick(ordre.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(accentFarve)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ordre.bordEllerTakeaway,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                color = LumiPOSColors.TekstLys,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${ordre.id} · DKK ${ordre.total.toInt()}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = LumiPOSColors.TekstDæmpet
                            )
                        }
                    }
                }
            }
            } // Lukker else block
        }
    }
}

@Composable
fun RowScope.KolonneListe(
    titel: String,
    ikonType: String,
    elementer: List<String>,
    accentFarve: Color
) {
    Column(modifier = Modifier.weight(1f)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            when (ikonType) {
                "clock" -> ClockIcon(size = 14.dp, color = accentFarve)
                "progress" -> ProgressCircleIcon(size = 14.dp, color = accentFarve)
                "check" -> CheckIcon(size = 14.dp, color = accentFarve)
                else -> Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(accentFarve))
            }
            Text(
                text = titel,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = LumiPOSColors.TekstDæmpet,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Scrollable list of orders
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            elementer.forEach { linje ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(accentFarve)
                        )
                        Text(
                            text = linje,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = LumiPOSColors.TekstLys,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.HøjreInfoPanel(
    borde: List<com.project.lumipos.model.Bord>,
    onBordClick: (Int) -> Unit = {}
) {
    val ledigeBorde = borde.filter { it.erLedig }
    var visLagerDialog by remember { mutableStateOf(false) }
    var lagerVarer by remember { 
        mutableStateOf(listOf(
            LagerVare("1", "Espresso bønner", LagerStatus.UDSOLGT, "coffee"),
            LagerVare("2", "Havremælk", LagerStatus.LAVT_LAGER, "drink"),
            LagerVare("3", "Croissant dej", LagerStatus.UDSOLGT, "bread")
        ))
    }
    
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Lagerstatus card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(LumiPOSColors.Rød.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            BoxIcon(size = 18.dp, color = LumiPOSColors.Rød)
                        }
                        Column {
                            Text(
                                text = "Lagerstatus",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                                color = LumiPOSColors.TekstLys,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${lagerVarer.size} varer",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = LumiPOSColors.TekstDæmpet
                            )
                        }
                    }
                    
                    // Administrer knap
                    IconButton(
                        onClick = { visLagerDialog = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text("⚙️", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable lagerstatus list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp, max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    lagerVarer.forEach { vare ->
                        LagerItem(
                            navn = vare.navn, 
                            status = vare.status.displayNavn, 
                            iconType = vare.iconType
                        )
                    }
                }
            }
        }

        // Borde card - LIVE DATA
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(LumiPOSColors.Grøn.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        TableIcon(size = 18.dp, color = LumiPOSColors.Grøn)
                    }
                    Column {
                        Text(
                            text = "Borde",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                            color = LumiPOSColors.TekstLys,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${ledigeBorde.size} ledige",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable borde list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp, max = 600.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    borde.forEach { bord ->
                        BordItem(
                            bord = "Bord ${bord.nummer}",
                            status = bord.status.displayNavn,
                            onClick = { onBordClick(bord.nummer) }
                        )
                    }
                }
            }
        }
    }
    
    // Lager Dialog
    if (visLagerDialog) {
        LagerDialog(
            varer = lagerVarer,
            onDismiss = { visLagerDialog = false },
            onAdd = { navn, status, iconType ->
                val newId = (lagerVarer.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
                lagerVarer = lagerVarer + LagerVare(newId.toString(), navn, status, iconType)
            },
            onEdit = { opdateretVare ->
                lagerVarer = lagerVarer.map { if (it.id == opdateretVare.id) opdateretVare else it }
            },
            onDelete = { id ->
                lagerVarer = lagerVarer.filter { it.id != id }
            }
        )
    }
}

@Composable
fun LagerItem(navn: String, status: String, iconType: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                when (iconType) {
                    "coffee" -> CoffeeIcon(size = 16.dp, color = LumiPOSColors.TekstDæmpet)
                    "drink" -> DrinkIcon(size = 16.dp, color = LumiPOSColors.TekstDæmpet)
                    "bread" -> BreadIcon(size = 16.dp, color = LumiPOSColors.TekstDæmpet)
                    else -> Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(LumiPOSColors.TekstDæmpet))
                }
                Text(
                    text = navn,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = LumiPOSColors.TekstLys
                )
            }
            Text(
                text = status,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = if (status == "Udsolgt") LumiPOSColors.Rød else LumiPOSColors.AccentBlå,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun BordItem(bord: String, status: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableIcon(size = 14.dp, color = LumiPOSColors.TekstDæmpet)
                Text(
                    text = bord,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = LumiPOSColors.TekstLys
                )
            }
            
            // Status med farvet brikke
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Farvet cirkel brikke
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when (status) {
                                "Ledig" -> LumiPOSColors.Grøn
                                "Optaget" -> LumiPOSColors.Rød
                                "Reserveret" -> LumiPOSColors.Orange
                                else -> LumiPOSColors.TekstDæmpet
                            }
                        )
                )
                
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    fontWeight = FontWeight.Medium,
                    color = when (status) {
                        "Ledig" -> LumiPOSColors.Grøn
                        "Optaget" -> LumiPOSColors.Rød
                        "Reserveret" -> LumiPOSColors.Orange
                        else -> LumiPOSColors.TekstDæmpet
                    }
                )
            }
        }
    }
}
