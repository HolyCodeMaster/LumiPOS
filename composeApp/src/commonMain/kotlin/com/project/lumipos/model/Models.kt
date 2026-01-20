package com.project.lumipos.model

import androidx.compose.ui.graphics.Color

// Ordre status model
data class OrdreStatus(
    val titel: String,
    val antal: Int,
    val beløb: String,
    val accent: Color,
    val ikon: String,
    val progress: Float
)

// Lager status model
data class LagerStatus(
    val navn: String,
    val status: String,
    val ikon: String
)

// Bord status model
data class BordStatus(
    val bord: String,
    val status: String,
    val ikon: String
)

// Produkt model
data class Produkt(
    val id: Int,
    val navn: String,
    val pris: Int,
    val kategori: String,
    val ikon: String
)

// Kurv item model
data class KurvItem(
    val produkt: Produkt,
    val antal: Int
)

// Navigation tabs
enum class NavigationTab {
    OVERSIGT,
    SALG
}
