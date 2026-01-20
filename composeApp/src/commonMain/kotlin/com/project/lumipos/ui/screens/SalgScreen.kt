package com.project.lumipos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.lumipos.model.Produkt
import com.project.lumipos.model.KurvItem
import com.project.lumipos.ui.components.KategoriChip
import com.project.lumipos.ui.components.ProduktKort
import com.project.lumipos.ui.components.KurvPanel

@Composable
fun SalgScreen(
    kurv: List<KurvItem>,
    onKurvOpdater: (List<KurvItem>) -> Unit
) {
    val produkter = remember {
        listOf(
            Produkt(1, "Espresso", 35, "Kaffe", "coffee"),
            Produkt(2, "Cappuccino", 42, "Kaffe", "coffee"),
            Produkt(3, "Latte", 45, "Kaffe", "coffee"),
            Produkt(4, "Americano", 38, "Kaffe", "coffee"),
            Produkt(5, "Cortado", 40, "Kaffe", "coffee"),
            Produkt(6, "Flat White", 48, "Kaffe", "coffee"),
            Produkt(7, "Croissant", 28, "Bagværk", "bread"),
            Produkt(8, "Wienerbrød", 32, "Bagværk", "bread"),
            Produkt(9, "Rundstykke", 15, "Bagværk", "bread"),
            Produkt(10, "Kanelstang", 30, "Bagværk", "bread"),
            Produkt(11, "Cola", 25, "Drikkevarer", "drink"),
            Produkt(12, "Vand", 20, "Drikkevarer", "drink"),
            Produkt(13, "Juice", 30, "Drikkevarer", "drink"),
            Produkt(14, "Mineralvand", 22, "Drikkevarer", "drink")
        )
    }

    var valgtKategori by remember { mutableStateOf("Alle") }
    val kategorier = listOf(
        Triple("Alle", "Alle", "all"),
        Triple("Kaffe", "Kaffe", "coffee"),
        Triple("Bagværk", "Bagværk", "bread"),
        Triple("Drikkevarer", "Drikkevarer", "drink")
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Venstre side: Produkter
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {
            // Kategori-filtre
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                kategorier.forEach { (label, kategori, iconType) ->
                    KategoriChip(
                        label = label,
                        iconType = iconType,
                        valgt = valgtKategori == kategori,
                        onClick = { valgtKategori = kategori }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Produkt grid
            val filtrereDeProdukter = if (valgtKategori == "Alle") produkter 
                else produkter.filter { it.kategori == valgtKategori }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtrereDeProdukter) { produkt ->
                    ProduktKort(
                        produkt = produkt,
                        onTilføj = {
                            val eksisterende = kurv.find { it.produkt.id == produkt.id }
                            val nyKurv = if (eksisterende != null) {
                                kurv.map { 
                                    if (it.produkt.id == produkt.id) 
                                        it.copy(antal = it.antal + 1) 
                                    else it 
                                }
                            } else {
                                kurv + KurvItem(produkt, 1)
                            }
                            onKurvOpdater(nyKurv)
                        }
                    )
                }
            }
        }

        // Højre side: Kurv
        KurvPanel(
            kurv = kurv,
            onKurvOpdater = onKurvOpdater
        )
    }
}
