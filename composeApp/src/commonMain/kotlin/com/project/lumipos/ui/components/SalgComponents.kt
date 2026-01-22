package com.project.lumipos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.project.lumipos.model.Produkt
import com.project.lumipos.model.KurvItem
import com.project.lumipos.ui.theme.LumiPOSColors

@Composable
fun KategoriChip(
    label: String,
    iconType: String,
    valgt: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.height(44.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (valgt) LumiPOSColors.AccentBlå else LumiPOSColors.SurfaceVariant,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconColor = if (valgt) Color.White else LumiPOSColors.TekstDæmpet
            when (iconType) {
                "all" -> AllItemsIcon(size = 16.dp, color = iconColor)
                "coffee" -> CoffeeIcon(size = 16.dp, color = iconColor)
                "bread" -> BreadIcon(size = 16.dp, color = iconColor)
                "drink" -> DrinkIcon(size = 16.dp, color = iconColor)
                else -> Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(iconColor))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = if (valgt) Color.White else LumiPOSColors.TekstDæmpet,
                fontWeight = if (valgt) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProduktKort(
    produkt: Produkt,
    onTilføj: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onTilføj() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LumiPOSColors.SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                when (produkt.ikon) {
                    "coffee" -> CoffeeIcon(size = 30.dp, color = LumiPOSColors.AccentBlå)
                    "bread" -> BreadIcon(size = 30.dp, color = LumiPOSColors.Orange)
                    "drink" -> DrinkIcon(size = 30.dp, color = LumiPOSColors.Grøn)
                    else -> Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(LumiPOSColors.TekstDæmpet))
                }
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = produkt.navn,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${produkt.pris} DKK",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                    color = LumiPOSColors.AccentBlå,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RowScope.KurvPanel(
    kurv: List<KurvItem>,
    onKurvOpdater: (List<KurvItem>) -> Unit,
    onBetal: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LumiPOSColors.Panel),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CartIcon(size = 20.dp, color = LumiPOSColors.AccentBlå)
                Text(
                    text = "Kurv",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = LumiPOSColors.TekstLys,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (kurv.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🛍️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Kurven er tom",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(kurv.size) { index ->
                        val item = kurv[index]
                        KurvItemRække(
                            item = item,
                            onAntalSkift = { nytAntal ->
                                val nyKurv = if (nytAntal <= 0) {
                                    kurv.filter { it.produkt.id != item.produkt.id }
                                } else {
                                    kurv.map { 
                                        if (it.produkt.id == item.produkt.id) 
                                            it.copy(antal = nytAntal) 
                                        else it 
                                    }
                                }
                                onKurvOpdater(nyKurv)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = LumiPOSColors.SurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Total
                val total = kurv.sumOf { it.produkt.pris * it.antal }
                val moms = (total * 0.25).toInt()

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Subtotal",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                        Text(
                            text = "${total - moms} DKK",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = LumiPOSColors.TekstLys
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Moms (25%)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = LumiPOSColors.TekstDæmpet
                        )
                        Text(
                            text = "$moms DKK",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                            color = LumiPOSColors.TekstLys
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            color = LumiPOSColors.TekstLys,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$total DKK",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            color = LumiPOSColors.AccentBlå,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Betal knap
                Button(
                    onClick = onBetal,
                    enabled = kurv.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LumiPOSColors.AccentBlå,
                        disabledContainerColor = LumiPOSColors.SurfaceVariant
                    )
                ) {
                    Text(
                        text = "Betal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun KurvItemRække(
    item: KurvItem,
    onAntalSkift: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.produkt.navn,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = LumiPOSColors.TekstLys,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${item.produkt.pris} DKK",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = LumiPOSColors.TekstDæmpet
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(LumiPOSColors.SurfaceVariant)
                    .clickable { onAntalSkift(item.antal - 1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "−",
                    color = LumiPOSColors.TekstLys,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${item.antal}",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = LumiPOSColors.TekstLys,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(LumiPOSColors.AccentBlå)
                    .clickable { onAntalSkift(item.antal + 1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
