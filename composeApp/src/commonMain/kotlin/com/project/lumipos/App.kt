package com.project.lumipos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class Vare(
    val navn: String,
    val pris: Double,
    val antal: Int = 1
)

@Composable
@Preview
fun App() {
    val darkColorScheme = darkColorScheme(
        primary = androidx.compose.ui.graphics.Color(0xFF6366F1),
        secondary = androidx.compose.ui.graphics.Color(0xFF8B5CF6),
        tertiary = androidx.compose.ui.graphics.Color(0xFFEC4899),
        background = androidx.compose.ui.graphics.Color(0xFF0F172A),
        surface = androidx.compose.ui.graphics.Color(0xFF1E293B),
        surfaceVariant = androidx.compose.ui.graphics.Color(0xFF334155),
        onPrimary = androidx.compose.ui.graphics.Color.White,
        onSecondary = androidx.compose.ui.graphics.Color.White,
        onBackground = androidx.compose.ui.graphics.Color(0xFFF1F5F9),
        onSurface = androidx.compose.ui.graphics.Color(0xFFE2E8F0),
        onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFCBD5E1)
    )

    MaterialTheme(colorScheme = darkColorScheme) {
        var kurv by remember { mutableStateOf(listOf<Vare>()) }
        var viserBekræftelse by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "POS System",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(getStandardVarer()) { vare ->
                        VareKort(
                            vare = vare,
                            onTilføj = { kurv = kurv + vare }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            KurvPanel(
                kurv = kurv,
                onFjern = { index -> kurv = kurv.toMutableList().apply { removeAt(index) } },
                onOpdaterAntal = { index, antal ->
                    kurv = kurv.toMutableList().apply {
                        set(index, this[index].copy(antal = antal))
                    }
                },
                onBetaling = {
                    viserBekræftelse = true
                },
                viserBekræftelse = viserBekræftelse
            )
        }

        if (viserBekræftelse) {
            LaunchedEffect(Unit) {
                delay(1000)
                kurv = emptyList()
                viserBekræftelse = false
            }
        }
    }
}

@Composable
fun VareKort(
    vare: Vare,
    onTilføj: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = vare.navn.take(1),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vare.navn,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${vare.pris} DKK",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onTilføj,
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Tilføj",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun KurvPanel(
    kurv: List<Vare>,
    onFjern: (Int) -> Unit,
    onOpdaterAntal: (Int, Int) -> Unit,
    onBetaling: () -> Unit,
    viserBekræftelse: Boolean
) {
    Card(
        modifier = Modifier
            .width(420.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(32.dp)),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Kurv",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (kurv.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Kurven er tom",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(kurv.size) { index ->
                        KurvItem(
                            vare = kurv[index],
                            onFjern = { onFjern(index) },
                            onOpdaterAntal = { antal ->
                                if (antal > 0) {
                                    onOpdaterAntal(index, antal)
                                } else {
                                    onFjern(index)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Divider(
                color = MaterialTheme.colorScheme.surfaceVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            val subtotal = kurv.sumOf { it.pris * it.antal }
            val moms = subtotal * 0.25
            val total = subtotal + moms

            KurvOpsummering(
                subtotal = subtotal,
                moms = moms,
                total = total,
                onBetaling = onBetaling,
                erKurvTom = kurv.isEmpty(),
                viserBekræftelse = viserBekræftelse
            )
        }
    }
}

@Composable
fun KurvItem(
    vare: Vare,
    onFjern: () -> Unit,
    onOpdaterAntal: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vare.navn,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${vare.pris} DKK",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { onOpdaterAntal(vare.antal - 1) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = "−",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${vare.antal}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { onOpdaterAntal(vare.antal + 1) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${vare.pris * vare.antal} DKK",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(80.dp),
                    textAlign = TextAlign.End
                )

                IconButton(
                    onClick = onFjern,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = "×",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun KurvOpsummering(
    subtotal: Double,
    moms: Double,
    total: Double,
    onBetaling: () -> Unit,
    erKurvTom: Boolean,
    viserBekræftelse: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OpsummeringRække("Subtotal", subtotal)
        OpsummeringRække("Moms (25%)", moms)
        
        Divider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$total DKK",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (viserBekræftelse) {
            Text(
                text = "Betaling gennemført!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onBetaling,
            enabled = !erKurvTom,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Betal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun OpsummeringRække(
    label: String,
    værdi: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$værdi DKK",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
    }
}

fun getStandardVarer(): List<Vare> {
    return listOf(
        Vare("Kaffe", 35.00),
        Vare("Croissant", 25.00),
        Vare("Sandwich", 65.00),
        Vare("Smoothie", 45.00),
        Vare("Kage", 40.00),
        Vare("The", 30.00),
        Vare("Juice", 28.00),
        Vare("Cookie", 15.00)
    )
}