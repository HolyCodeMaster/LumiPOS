package com.project.lumipos.viewmodel

import com.project.lumipos.data.OrderRepository
import com.project.lumipos.model.*
import kotlinx.coroutines.flow.*

/**
 * LumiViewModel - Central business logic og state management
 * Kobler UI med OrderRepository
 */
class LumiViewModel(
    private val repository: OrderRepository = OrderRepository.instance
) {
    
    // ═══════════════════════════════════════════════════════════
    // STATE FLOWS (Observable data for UI)
    // ═══════════════════════════════════════════════════════════
    
    val allOrders: StateFlow<List<Ordre>> = repository.ordrer
    val borde: StateFlow<List<Bord>> = repository.borde
    val transaktioner: StateFlow<List<Transaktion>> = repository.transaktioner
    
    // Derived state - Ordrer grupperet efter status
    val ordrerVenterBetaling: StateFlow<List<Ordre>> = allOrders.map { ordrer ->
        ordrer.filter { it.status == OrdreStatusType.VENTER_BETALING }
    }.stateIn(
        scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    
    val ordrerIGang: StateFlow<List<Ordre>> = allOrders.map { ordrer ->
        ordrer.filter { it.status == OrdreStatusType.I_GANG }
    }.stateIn(
        scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    
    val ordrerBetalt: StateFlow<List<Ordre>> = allOrders.map { ordrer ->
        ordrer.filter { it.status == OrdreStatusType.BETALT }
    }.stateIn(
        scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    
    // Dagens statistik
    val dagensStatistik: StateFlow<DagensStatistik> = allOrders.map {
        repository.generateDagensStatistik()
    }.stateIn(
        scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = DagensStatistik(
            datoString = "2026-01-20",
            antalOrdrer = 0,
            omsætning = 0,
            gennemsnitOrdre = 0,
            topProdukter = emptyList(),
            ordrerPerStatus = emptyMap()
        )
    )
    
    // Bord oversigt
    val ledigeBorde: StateFlow<List<Bord>> = borde.map { alleBorde ->
        alleBorde.filter { it.erLedig }
    }.stateIn(
        scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    
    val optagedeBorde: StateFlow<List<Bord>> = borde.map { alleBorde ->
        alleBorde.filter { it.erOptaget }
    }.stateIn(
        scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default),
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
    
    // ═══════════════════════════════════════════════════════════
    // ORDRE OPERATIONS
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Opret ny ordre fra kurv
     * @return Ordre ID hvis succesfuld, null hvis fejl
     */
    fun placeOrder(
        items: List<KurvItem>,
        bordNr: Int?, // null = Takeaway
        bemærkninger: String = ""
    ): Result<Ordre> {
        return try {
            if (items.isEmpty()) {
                return Result.failure(Exception("Kurven er tom"))
            }
            
            // Check om bord er ledigt
            bordNr?.let {
                val bord = repository.getBord(it)
                if (bord == null || !bord.erLedig) {
                    return Result.failure(Exception("Bord $it er ikke ledigt"))
                }
            }
            
            val ordre = repository.createOrder(items, bordNr, bemærkninger)
            Result.success(ordre)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Opdater ordre status
     */
    fun updateOrderStatus(ordreId: String, nyStatus: OrdreStatusType): Result<Unit> {
        return try {
            repository.updateOrderStatus(ordreId, nyStatus)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Marker ordre som "I gang"
     */
    fun startOrder(ordreId: String): Result<Unit> {
        return updateOrderStatus(ordreId, OrdreStatusType.I_GANG)
    }
    
    /**
     * Marker ordre som "Klar"
     */
    fun completeOrder(ordreId: String): Result<Unit> {
        return updateOrderStatus(ordreId, OrdreStatusType.KLAR)
    }
    
    /**
     * Registrer betaling og marker ordre som betalt
     */
    fun processPayment(
        ordreId: String,
        betalingsmetode: BetalingsMetode = BetalingsMetode.KONTANT
    ): Result<Transaktion> {
        return try {
            val transaktion = repository.registerPayment(ordreId, betalingsmetode)
            if (transaktion != null) {
                Result.success(transaktion)
            } else {
                Result.failure(Exception("Kunne ikke finde ordre"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Annuller ordre
     */
    fun cancelOrder(ordreId: String): Result<Unit> {
        return try {
            repository.cancelOrder(ordreId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Hent specifik ordre
     */
    fun getOrder(ordreId: String): Ordre? {
        return repository.getOrder(ordreId)
    }
    
    fun getAllOrders(): List<Ordre> {
        return repository.getTodaysOrders()
    }
    
    // ═══════════════════════════════════════════════════════════
    // BORD OPERATIONS
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Reserver et bord
     */
    fun reserverBord(bordNr: Int, navn: String): Result<Unit> {
        return try {
            repository.reserverBord(bordNr, navn)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Hent bord info
     */
    fun getBord(bordNr: Int): Bord? {
        return repository.getBord(bordNr)
    }
    
    // ═══════════════════════════════════════════════════════════
    // RAPPORT OPERATIONS
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Generer X-Rapport (mellemrapport)
     */
    fun generateXRapport(): XRapport {
        return repository.generateXRapport()
    }
    
    /**
     * Generer Z-Rapport (daglig afslutning)
     * Kræver admin godkendelse i UI
     */
    fun generateZRapport(adminPin: String): Result<ZRapport> {
        return try {
            // Simpel PIN check - i produktion ville dette være mere sikker
            if (adminPin != "1234") {
                return Result.failure(Exception("Forkert admin PIN"))
            }
            
            val today = "2026-01-20"
            
            // Check om Z-rapport allerede er genereret i dag
            val historiske = repository.historiskeRapporter.value
            val harZrapportIDag = historiske.any { it.datoString == today }
            
            if (harZrapportIDag) {
                return Result.failure(Exception("Z-rapport er allerede genereret i dag"))
            }
            
            val zRapport = repository.generateZRapport()
            Result.success(zRapport)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Hent historiske rapporter
     */
    fun getHistoriskeRapporter(): List<HistoriskRapport> {
        return repository.historiskeRapporter.value
    }
    
    // ═══════════════════════════════════════════════════════════
    // STATISTIK HELPERS
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Hent dagens omsætning
     */
    fun getDagensOmsætning(): Int {
        return dagensStatistik.value.omsætning
    }
    
    /**
     * Hent antal ordrer i dag
     */
    fun getAntalOrdrerIDag(): Int {
        return dagensStatistik.value.antalOrdrer
    }
    
    /**
     * Hent gennemsnits ordre størrelse
     */
    fun getGennemsnitOrdreStørrelse(): Int {
        return dagensStatistik.value.gennemsnitOrdre
    }
}
