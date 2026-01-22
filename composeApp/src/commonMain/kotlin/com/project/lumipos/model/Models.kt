package com.project.lumipos.model

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════
// DISPLAY MODELS (UI)
// ═══════════════════════════════════════════════════════════

// Ordre status model (til dashboard display)
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

// Bord status model (forenklet til display)
data class BordStatus(
    val bord: String,
    val status: String,
    val ikon: String
)

// ═══════════════════════════════════════════════════════════
// CORE BUSINESS MODELS
// ═══════════════════════════════════════════════════════════

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
) {
    val total: Int get() = produkt.pris * antal
}

// Ordre - Hovedmodel for alle ordrer
data class Ordre(
    val id: String,
    val items: List<KurvItem>,
    val bordNr: Int?, // null = Takeaway
    val status: OrdreStatusType,
    val oprettet: Long = 0L,
    val opdateret: Long = 0L,
    val bemærkninger: String = ""
) {
    val subtotal: Int get() = items.sumOf { it.total }
    val moms: Int get() = (subtotal * 0.25).toInt()
    val total: Int get() = subtotal + moms
    
    val erTakeaway: Boolean get() = bordNr == null
    val bordEllerTakeaway: String get() = if (erTakeaway) "Takeaway" else "Bord $bordNr"
    
    fun medStatus(nyStatus: OrdreStatusType): Ordre {
        return copy(status = nyStatus, opdateret = 0L)
    }
}

// Ordre status types
enum class OrdreStatusType(val displayNavn: String) {
    VENTER_BETALING("Venter på betaling"),
    I_GANG("I gang"),
    KLAR("Klar til afhentning"),
    BETALT("Betalt"),
    ANNULLERET("Annulleret");
    
    val erAktiv: Boolean get() = this != BETALT && this != ANNULLERET
}

// Bord state - Holder styr på borde
data class Bord(
    val nummer: Int,
    val status: BordStatusType = BordStatusType.LEDIG,
    val aktuelOrdreId: String? = null,
    val reserveretTil: String? = null
) {
    val erLedig: Boolean get() = status == BordStatusType.LEDIG
    val erOptaget: Boolean get() = status == BordStatusType.OPTAGET
}

enum class BordStatusType(val displayNavn: String, val color: Color) {
    LEDIG("Ledig", Color(0xFF22C55E)),
    OPTAGET("Optaget", Color(0xFFEF4444)),
    RESERVERET("Reserveret", Color(0xFFF97316))
}

// Transaktion - Log over alle betalinger
data class Transaktion(
    val id: String,
    val ordreId: String,
    val beløb: Int,
    val betalingsmetode: BetalingsMetode,
    val tidspunkt: Long = 0L
)

enum class BetalingsMetode(val displayNavn: String) {
    KONTANT("Kontant"),
    KORT("Kort"),
    MOBILEPAY("MobilePay"),
    ONLINE_BESTILLING("Online Bestilling"),
    GAVEKORT("Gavekort")
}

// ═══════════════════════════════════════════════════════════
// RAPPORT MODELS
// ═══════════════════════════════════════════════════════════

// Dagens statistik
data class DagensStatistik(
    val datoString: String, // Format: "YYYY-MM-DD"
    val antalOrdrer: Int,
    val omsætning: Int,
    val gennemsnitOrdre: Int,
    val topProdukter: List<Pair<String, Int>>,
    val ordrerPerStatus: Map<OrdreStatusType, Int>
) {
    val moms: Int get() = (omsætning * 0.25 / 1.25).toInt()
    val netto: Int get() = omsætning - moms
}

// X-Rapport (mellemrapport)
data class XRapport(
    val genereret: Long,
    val statistik: DagensStatistik,
    val aktiveBorde: Int,
    val aktiveOrdrer: Int
)

// Z-Rapport (daglig afslutning)
data class ZRapport(
    val genereret: Long,
    val statistik: DagensStatistik,
    val afsluttetAf: String = "Admin"
)

// Historisk rapport (gemt Z-rapport)
data class HistoriskRapport(
    val id: String,
    val datoString: String, // Format: "YYYY-MM-DD"
    val zRapport: ZRapport
)

// ═══════════════════════════════════════════════════════════
// NAVIGATION
// ═══════════════════════════════════════════════════════════

enum class NavigationTab {
    OVERSIGT,
    SALG,
    HISTORIK,
    RAPPORTER
}
