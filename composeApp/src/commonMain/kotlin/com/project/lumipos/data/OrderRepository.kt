package com.project.lumipos.data

import com.project.lumipos.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * OrderRepository - Central datahåndtering for ordrer, borde og transaktioner
 * In-memory implementation med StateFlow for reactive updates
 */
class OrderRepository {
    
    // ═══════════════════════════════════════════════════════════
    // STATE
    // ═══════════════════════════════════════════════════════════
    
    internal val _ordrer = MutableStateFlow<List<Ordre>>(emptyList())
    val ordrer: StateFlow<List<Ordre>> = _ordrer.asStateFlow()
    
    internal val _borde = MutableStateFlow<List<Bord>>(initializeBorde())
    val borde: StateFlow<List<Bord>> = _borde.asStateFlow()
    
    internal val _transaktioner = MutableStateFlow<List<Transaktion>>(emptyList())
    val transaktioner: StateFlow<List<Transaktion>> = _transaktioner.asStateFlow()
    
    internal val _historiskeRapporter = MutableStateFlow<List<HistoriskRapport>>(emptyList())
    val historiskeRapporter: StateFlow<List<HistoriskRapport>> = _historiskeRapporter.asStateFlow()
    
    // Ordre counter - beregnes dynamisk baseret på eksisterende ordrer
    private fun getNextOrderNumber(): Int {
        val existingOrders = _ordrer.value
        if (existingOrders.isEmpty()) return 1
        
        // Find højeste ordre nummer
        val maxNumber = existingOrders.mapNotNull { ordre ->
            // Parse "#2026-0001" -> 1
            ordre.id.substringAfter("-").toIntOrNull()
        }.maxOrNull() ?: 0
        
        return maxNumber + 1
    }
    
    // Transaktion counter
    private fun getNextTransaktionNumber(): Int {
        val existingTransactions = _transaktioner.value
        if (existingTransactions.isEmpty()) return 1
        
        val maxNumber = existingTransactions.mapNotNull { trx ->
            // Parse "TRX-1" -> 1
            trx.id.substringAfter("-").toIntOrNull()
        }.maxOrNull() ?: 0
        
        return maxNumber + 1
    }
    
    // Z-rapport counter
    private fun getNextZRapportNumber(): Int {
        val existingReports = _historiskeRapporter.value
        if (existingReports.isEmpty()) return 1
        
        val maxNumber = existingReports.mapNotNull { rapport ->
            // Parse "Z-1" -> 1
            rapport.id.substringAfter("-").toIntOrNull()
        }.maxOrNull() ?: 0
        
        return maxNumber + 1
    }
    
    // ═══════════════════════════════════════════════════════════
    // ORDRE OPERATIONS
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Opret ny ordre
     */
    fun createOrder(
        items: List<KurvItem>,
        bordNr: Int?,
        bemærkninger: String = ""
    ): Ordre {
        val ordreId = generateOrderId()
        val nyOrdre = Ordre(
            id = ordreId,
            items = items,
            bordNr = bordNr,
            status = OrdreStatusType.VENTER_BETALING,
            bemærkninger = bemærkninger
        )
        
        _ordrer.value = _ordrer.value + nyOrdre
        
        // Opdater bord status hvis det er et bord
        bordNr?.let { optagerBord(it, ordreId) }
        
        return nyOrdre
    }
    
    /**
     * Opdater ordre status
     */
    fun updateOrderStatus(ordreId: String, nyStatus: OrdreStatusType) {
        _ordrer.value = _ordrer.value.map { ordre ->
            if (ordre.id == ordreId) {
                val opdateretOrdre = ordre.medStatus(nyStatus)
                
                // Frigør bord hvis ordre er betalt
                if (nyStatus == OrdreStatusType.BETALT) {
                    ordre.bordNr?.let { frigørBord(it) }
                }
                
                opdateretOrdre
            } else {
                ordre
            }
        }
    }
    
    /**
     * Annuller ordre
     */
    fun cancelOrder(ordreId: String) {
        updateOrderStatus(ordreId, OrdreStatusType.ANNULLERET)
    }
    
    /**
     * Hent ordre efter ID
     */
    fun getOrder(ordreId: String): Ordre? {
        return _ordrer.value.find { it.id == ordreId }
    }
    
    /**
     * Hent ordrer efter status
     */
    fun getOrdersByStatus(status: OrdreStatusType): List<Ordre> {
        return _ordrer.value.filter { it.status == status }
    }
    
    /**
     * Hent alle aktive ordrer (ikke betalt/annulleret)
     */
    fun getActiveOrders(): List<Ordre> {
        return _ordrer.value.filter { it.status.erAktiv }
    }
    
    /**
     * Hent ordrer for en specifik dato (YYYY-MM-DD format)
     */
    fun getOrdersForDate(dateString: String): List<Ordre> {
        return _ordrer.value // Simplified - return all for now
    }
    
    /**
     * Hent dagens ordrer
     */
    /**
     * Returner ALLE dagens ordrer (inkl. Venter, I gang, Klar, Betalt)
     * - Bruges til rapporter og statistik
     * - Tæller ALT omsætning fra alle aktive og afsluttede ordrer
     */
    fun getTodaysOrders(): List<Ordre> {
        // Inkluder ALLE ordrer undtagen ANNULLERET
        return _ordrer.value.filter { 
            it.status != OrdreStatusType.ANNULLERET 
        }
    }
    
    /**
     * Returner kun betalte ordrer
     */
    fun getPaidOrders(): List<Ordre> {
        return _ordrer.value.filter { it.status == OrdreStatusType.BETALT }
    }
    
    // ═══════════════════════════════════════════════════════════
    // BORD OPERATIONS
    // ═══════════════════════════════════════════════════════════
    
    private fun optagerBord(bordNr: Int, ordreId: String) {
        _borde.value = _borde.value.map { bord ->
            if (bord.nummer == bordNr) {
                bord.copy(
                    status = BordStatusType.OPTAGET,
                    aktuelOrdreId = ordreId
                )
            } else {
                bord
            }
        }
    }
    
    private fun frigørBord(bordNr: Int) {
        _borde.value = _borde.value.map { bord ->
            if (bord.nummer == bordNr) {
                bord.copy(
                    status = BordStatusType.LEDIG,
                    aktuelOrdreId = null
                )
            } else {
                bord
            }
        }
    }
    
    fun reserverBord(bordNr: Int, navn: String) {
        _borde.value = _borde.value.map { bord ->
            if (bord.nummer == bordNr && bord.erLedig) {
                bord.copy(
                    status = BordStatusType.RESERVERET,
                    reserveretTil = navn
                )
            } else {
                bord
            }
        }
    }
    
    fun getBord(bordNr: Int): Bord? {
        return _borde.value.find { it.nummer == bordNr }
    }
    
    fun getLedigeBorde(): List<Bord> {
        return _borde.value.filter { it.erLedig }
    }
    
    // ═══════════════════════════════════════════════════════════
    // TRANSAKTION OPERATIONS
    // ═══════════════════════════════════════════════════════════
    
    fun registerPayment(
        ordreId: String,
        betalingsmetode: BetalingsMetode
    ): Transaktion? {
        val ordre = getOrder(ordreId) ?: return null
        
        val transaktion = Transaktion(
            id = "TRX-${getNextTransaktionNumber()}",
            ordreId = ordreId,
            beløb = ordre.total,
            betalingsmetode = betalingsmetode
        )
        
        _transaktioner.value = _transaktioner.value + transaktion
        updateOrderStatus(ordreId, OrdreStatusType.BETALT)
        
        return transaktion
    }
    
    // ═══════════════════════════════════════════════════════════
    // STATISTIK & RAPPORTER
    // ═══════════════════════════════════════════════════════════
    
    fun generateDagensStatistik(dateString: String = "2026-01-20"): DagensStatistik {
        val ordrerForDag = getTodaysOrders()
        
        val omsætning = ordrerForDag.sumOf { it.total }
        val antalOrdrer = ordrerForDag.size
        val gennemsnit = if (antalOrdrer > 0) omsætning / antalOrdrer else 0
        
        // Top produkter
        val produktCount = mutableMapOf<String, Int>()
        ordrerForDag.forEach { ordre ->
            ordre.items.forEach { item ->
                produktCount[item.produkt.navn] = 
                    (produktCount[item.produkt.navn] ?: 0) + item.antal
            }
        }
        val topProdukter = produktCount.entries
            .sortedByDescending { it.value }
            .take(5)
            .map { it.key to it.value }
        
        // Ordrer per status
        val ordrerPerStatus = OrdreStatusType.values().associateWith { status ->
            getTodaysOrders().count { it.status == status }
        }
        
        return DagensStatistik(
            datoString = dateString,
            antalOrdrer = antalOrdrer,
            omsætning = omsætning,
            gennemsnitOrdre = gennemsnit,
            topProdukter = topProdukter,
            ordrerPerStatus = ordrerPerStatus
        )
    }
    
    fun generateXRapport(): XRapport {
        val statistik = generateDagensStatistik()
        val aktiveBorde = _borde.value.count { it.erOptaget }
        val aktiveOrdrer = getActiveOrders().size
        
        return XRapport(
            genereret = 0L,
            statistik = statistik,
            aktiveBorde = aktiveBorde,
            aktiveOrdrer = aktiveOrdrer
        )
    }
    
    fun generateZRapport(): ZRapport {
        val statistik = generateDagensStatistik()
        
        val zRapport = ZRapport(
            genereret = 0L,
            statistik = statistik
        )
        
        // Gem historisk
        val historisk = HistoriskRapport(
            id = "Z-${getNextZRapportNumber()}",
            datoString = statistik.datoString,
            zRapport = zRapport
        )
        _historiskeRapporter.value = _historiskeRapporter.value + historisk
        
        // Nulstil dagens ordrer (marker som arkiveret)
        // I en rigtig app ville vi flytte dem til en separat arkiv-tabel
        
        return zRapport
    }
    
    // ═══════════════════════════════════════════════════════════
    // HELPER FUNCTIONS
    // ═══════════════════════════════════════════════════════════
    
    private fun generateOrderId(): String {
        val year = 2026
        val id = getNextOrderNumber()
        return "#$year-${id.toString().padStart(4, '0')}"
    }
    
    companion object {
        private fun initializeBorde(): List<Bord> {
            return (1..20).map { Bord(nummer = it) }
        }
        
        // Singleton instance
        val instance = OrderRepository()
    }
}
