package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun StatusKortRække() {
    val kort = listOf(
        OrdreStatus("Venter på betaling", 8, "DKK 3.420", LumiPOSColors.AccentBlå, "clock", 0.65f),
        OrdreStatus("I gang", 5, "DKK 1.980", LumiPOSColors.Orange, "progress", 0.45f),
        OrdreStatus("Afsluttede ordrer", 24, "DKK 12.750", LumiPOSColors.Grøn, "check", 0.85f)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        kort.forEach { status ->
            StatusKort(status = status, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RowScope.StatusKort(
    status: OrdreStatus,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
fun RowScope.VentendeOgIGangPanel() {
    Card(
        modifier = Modifier
            .weight(1.5f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
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
                Text(
                    text = "Seneste 30 min",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = LumiPOSColors.TekstDæmpet
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                KolonneListe(
                    titel = "Venter på betaling",
                    ikonType = "clock",
                    elementer = listOf(
                        "Bord 4 · DKK 320",
                        "Takeaway #2031 · DKK 145",
                        "Bord 7 · DKK 610",
                        "Bord 12 · DKK 485",
                        "Takeaway #2032 · DKK 215"
                    ),
                    accentFarve = LumiPOSColors.AccentBlå
                )

                KolonneListe(
                    titel = "I gang",
                    ikonType = "progress",
                    elementer = listOf(
                        "Bord 1 · Hovedret",
                        "Bord 5 · Forret",
                        "Bord 9 · Drikkevarer",
                        "Bord 3 · Dessert",
                        "Takeaway #2030 · Pakkes"
                    ),
                    accentFarve = LumiPOSColors.Orange
                )
            }
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

        elementer.forEach { linje ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
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

@Composable
fun RowScope.HøjreInfoPanel() {
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
                            text = "3 varer",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                listOf(
                    Triple("Espresso bønner", "Udsolgt", "coffee"),
                    Triple("Havremælk", "Lavt lager", "drink"),
                    Triple("Croissant dej", "Udsolgt", "bread")
                ).forEach { (navn, status, iconType) ->
                    LagerItem(navn = navn, status = status, iconType = iconType)
                }
            }
        }

        // Borde card
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
                            text = "8 ledige",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        "Bord 2" to "Ledigt",
                        "Bord 3" to "Ledigt",
                        "Bord 6" to "Reserveret",
                        "Bord 8" to "Ledigt",
                        "Bord 10" to "Ledigt",
                        "Bord 11" to "Ledigt"
                    ).forEach { (bord, status) ->
                        BordItem(bord = bord, status = status)
                    }
                }
            }
        }
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
fun BordItem(bord: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableIcon(size = 14.dp, color = LumiPOSColors.TekstDæmpet)
                Text(
                    text = bord,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                    color = LumiPOSColors.TekstLys
                )
            }
            Text(
                text = status,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = when (status) {
                    "Ledigt" -> LumiPOSColors.Grøn
                    "Reserveret" -> LumiPOSColors.Orange
                    else -> LumiPOSColors.Rød
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
