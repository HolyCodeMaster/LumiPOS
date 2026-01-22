package com.project.lumipos.ui.screens

import androidx.compose.foundation.background
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
import com.project.lumipos.model.*
import com.project.lumipos.ui.theme.LumiPOSColors
import com.project.lumipos.ui.components.StatusKortRække
import com.project.lumipos.ui.components.VentendeOgIGangPanel
import com.project.lumipos.ui.components.HøjreInfoPanel
import com.project.lumipos.viewmodel.LumiViewModel

@Composable
fun OversigtScreen(viewModel: LumiViewModel) {
    var valgtFilter by remember { mutableStateOf("I dag") }
    var valgtOrdre by remember { mutableStateOf<Ordre?>(null) }
    var printOrdre by remember { mutableStateOf<Ordre?>(null) }
    var valgtBordNr by remember { mutableStateOf<Int?>(null) }
    var visOrdreListeDialog by remember { mutableStateOf(false) }
    var dialogTitel by remember { mutableStateOf("") }
    var dialogOrdrer by remember { mutableStateOf<List<Ordre>>(emptyList()) }
    var dialogFarve by remember { mutableStateOf(LumiPOSColors.AccentBlå) }
    val toastState = com.project.lumipos.ui.components.rememberToastState()
    
    // Collect data from ViewModel
    val dagensStatistik by viewModel.dagensStatistik.collectAsState()
    val ordrerVenter by viewModel.ordrerVenterBetaling.collectAsState()
    val ordrerIGang by viewModel.ordrerIGang.collectAsState()
    val borde by viewModel.borde.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // TEST MARKØR - Hvis du ser denne tekst virker den nye version!
        Text(
            text = "✅ v3.4 - UNIQUE IDS FIXED! ✅",
            style = MaterialTheme.typography.bodyMedium,
            color = LumiPOSColors.Grøn,
            modifier = Modifier.padding(4.dp)
        )
        
        OversigtHeader(
            valgtFilter = valgtFilter,
            onFilterSkift = { valgtFilter = it },
            omsætning = dagensStatistik.omsætning
        )

        StatusKortRække(
            ordrerVenter = ordrerVenter,
            ordrerIGang = ordrerIGang,
            ordrerBetalt = dagensStatistik.antalOrdrer,
            onKortKlik = { statusType ->
                when (statusType) {
                    "VENTER" -> {
                        dialogTitel = "Venter på betaling"
                        dialogOrdrer = ordrerVenter
                        dialogFarve = LumiPOSColors.AccentBlå
                        visOrdreListeDialog = true
                    }
                    "I_GANG" -> {
                        dialogTitel = "Ordrer i gang"
                        dialogOrdrer = ordrerIGang
                        dialogFarve = LumiPOSColors.Orange
                        visOrdreListeDialog = true
                    }
                    "BETALT" -> {
                        val betalteOrdrer = viewModel.getAllOrders().filter { it.status == OrdreStatusType.BETALT }
                        dialogTitel = "Afsluttede ordrer"
                        dialogOrdrer = betalteOrdrer
                        dialogFarve = LumiPOSColors.Grøn
                        visOrdreListeDialog = true
                    }
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 700.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            VentendeOgIGangPanel(
                ordrerVenter = ordrerVenter,
                ordrerIGang = ordrerIGang,
                onOrderClick = { ordreId ->
                    valgtOrdre = viewModel.getOrder(ordreId)
                },
                onHeaderClick = {
                    dialogTitel = "Alle Aktive Ordrer"
                    dialogOrdrer = ordrerVenter + ordrerIGang
                    dialogFarve = LumiPOSColors.AccentBlå
                    visOrdreListeDialog = true
                }
            )
            HøjreInfoPanel(
                borde = borde,
                onBordClick = { bordNr -> valgtBordNr = bordNr }
            )
        }
    }
    
    // Ordre detaljer dialog
    valgtOrdre?.let { ordre ->
        com.project.lumipos.ui.components.OrdreDetaljerDialog(
            ordre = ordre,
            onDismiss = { valgtOrdre = null },
            onMarkerSomBetalt = { ordreId ->
                viewModel.processPayment(ordreId)
                valgtOrdre = null
                toastState.show("Ordre $ordreId markeret som betalt", com.project.lumipos.ui.components.ToastType.SUCCESS)
            },
            onMarkerSomIGang = { ordreId ->
                viewModel.startOrder(ordreId)
                valgtOrdre = null
                toastState.show("Ordre $ordreId er nu i gang", com.project.lumipos.ui.components.ToastType.INFO)
            },
            onMarkerSomKlar = { ordreId ->
                viewModel.completeOrder(ordreId)
                valgtOrdre = null
                toastState.show("Ordre $ordreId er klar til betaling", com.project.lumipos.ui.components.ToastType.SUCCESS)
            },
            onAnnuller = { ordreId ->
                viewModel.cancelOrder(ordreId)
                toastState.show("Ordre $ordreId annulleret", com.project.lumipos.ui.components.ToastType.WARNING)
            },
            onPrint = { ordre ->
                printOrdre = ordre
            }
        )
    }
    
    // Print dialog
    printOrdre?.let { ordre ->
        com.project.lumipos.ui.components.PrintKvitteringDialog(
            ordre = ordre,
            onDismiss = { printOrdre = null }
        )
    }
    
    // Bord info dialog
    valgtBordNr?.let { bordNr ->
        val bord = viewModel.getBord(bordNr)
        bord?.let {
            val ordre = it.aktuelOrdreId?.let { ordreId -> viewModel.getOrder(ordreId) }
            com.project.lumipos.ui.components.BordInfoDialog(
                bord = it,
                ordre = ordre,
                onDismiss = { valgtBordNr = null },
                onVisOrdre = { ordreId ->
                    valgtBordNr = null
                    valgtOrdre = viewModel.getOrder(ordreId)
                },
                onReserver = {
                    viewModel.reserverBord(bordNr, "Gæst")
                    valgtBordNr = null
                    toastState.show("Bord $bordNr reserveret", com.project.lumipos.ui.components.ToastType.INFO)
                },
                onFrigør = {
                    valgtBordNr = null
                    toastState.show("Bord $bordNr frigjort", com.project.lumipos.ui.components.ToastType.SUCCESS)
                }
            )
        }
    }
    
    // Ordre liste dialog
    if (visOrdreListeDialog) {
        com.project.lumipos.ui.components.OrdreListeDialog(
            titel = dialogTitel,
            ordrer = dialogOrdrer,
            accentFarve = dialogFarve,
            onDismiss = { visOrdreListeDialog = false },
            onOrdreKlik = { ordreId ->
                valgtOrdre = viewModel.getOrder(ordreId)
            }
        )
    }
    
    // Toast notifications
    Box(modifier = Modifier.fillMaxSize()) {
        com.project.lumipos.ui.components.Toast(
            message = toastState.message,
            type = toastState.type,
            visible = toastState.visible,
            onDismiss = { toastState.dismiss() }
        )
    }
}

@Composable
fun OversigtHeader(
    valgtFilter: String,
    onFilterSkift: (String) -> Unit,
    omsætning: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                com.project.lumipos.ui.components.MoneyIcon(size = 16.dp, color = LumiPOSColors.AccentBlå)
                Column {
                    Text(
                        text = "Samlet indtjening",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                        color = LumiPOSColors.TekstDæmpet,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${omsætning} DKK",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp),
                        color = LumiPOSColors.AccentBlå,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Oversigt over dagens aktivitet",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = LumiPOSColors.TekstDæmpet.copy(alpha = 0.7f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(label = "I dag", valgt = valgtFilter == "I dag", onClick = { onFilterSkift("I dag") })
            FilterChip(label = "Uge", valgt = valgtFilter == "Uge", onClick = { onFilterSkift("Uge") })
            FilterChip(label = "Måned", valgt = valgtFilter == "Måned", onClick = { onFilterSkift("Måned") })
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    valgt: Boolean,
    onClick: () -> Unit
) {
    val baggrund = if (valgt) LumiPOSColors.AccentBlå.copy(alpha = 0.15f) else Color.Transparent
    val kant = if (valgt) LumiPOSColors.AccentBlå else LumiPOSColors.SurfaceVariant
    val tekst = if (valgt) LumiPOSColors.AccentBlå else LumiPOSColors.TekstDæmpet

    Surface(
        modifier = Modifier.height(34.dp),
        shape = RoundedCornerShape(16.dp),
        color = baggrund,
        border = androidx.compose.foundation.BorderStroke(1.dp, kant),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                color = tekst,
                fontWeight = if (valgt) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
