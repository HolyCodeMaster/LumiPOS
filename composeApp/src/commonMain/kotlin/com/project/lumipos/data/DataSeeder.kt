package com.project.lumipos.data

import com.project.lumipos.model.*
import kotlin.random.Random

/**
 * DataSeeder - Genererer realistisk testdata
 * Bruges til at fylde systemet med eksempel-ordrer
 */
object DataSeeder {
    
    private val produkter = listOf(
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
    
    /**
     * Seed systemet med testdata
     * @param daysBack Antal dage tilbage at generere data for
     * @param todayActiveOrders Antal aktive ordrer for i dag
     */
    fun seed(
        repository: OrderRepository,
        todayActiveOrders: Int = 8
    ) {
        println("🌱 DataSeeder: Starter seeding...")
        
        // Generer dagens betalte ordrer (simplified)
        repeat(15) {
            val items = generateRandomItems(count = Random.nextInt(1, 4))
            val bordNr = if (Random.nextDouble() < 0.7) Random.nextInt(1, 21) else null
            
            val ordre = repository.createOrder(
                items = items,
                bordNr = bordNr,
                bemærkninger = ""
            )
            // Opdater status til BETALT
            repository.updateOrderStatus(ordre.id, OrdreStatusType.BETALT)
        }
        
        // Generer aktive ordrer for i dag
        generateTodaysActiveOrders(repository, count = todayActiveOrders)
        
        println("✅ DataSeeder: Seeding afsluttet!")
        println("   📊 Total ordrer: ${repository.ordrer.value.size}")
        println("   🟢 Aktive ordrer: ${repository.getActiveOrders().size}")
        println("   💰 Dagens omsætning: ${repository.generateDagensStatistik().omsætning} DKK")
    }
    
    
    /**
     * Generer aktive ordrer for i dag (diverse statusser)
     * VIGTIGT: Et bord kan kun have ÉN aktiv ordre ad gangen!
     */
    private fun generateTodaysActiveOrders(
        repository: OrderRepository,
        count: Int
    ) {
        val brugteBorde = mutableSetOf<Int>() // Track hvilke borde der allerede har ordrer
        
        // 8 ordrer "Venter på betaling"
        repeat(8) {
            val items = generateRandomItems(count = Random.nextInt(1, 4))
            
            // Vælg et bord der IKKE allerede har en ordre
            val bordNr = if (Random.nextDouble() < 0.7) {
                var valgtBord: Int
                do {
                    valgtBord = Random.nextInt(1, 21)
                } while (brugteBorde.contains(valgtBord))
                brugteBorde.add(valgtBord)
                valgtBord
            } else {
                null // Takeaway
            }
            
            repository.createOrder(
                items = items,
                bordNr = bordNr,
                bemærkninger = ""
            )
        }
        
        // 6 ordrer "I gang"
        repeat(6) {
            val items = generateRandomItems(count = Random.nextInt(2, 5))
            
            // Vælg et bord der IKKE allerede har en ordre
            val bordNr = if (Random.nextDouble() < 0.7) {
                var valgtBord: Int
                do {
                    valgtBord = Random.nextInt(1, 21)
                } while (brugteBorde.contains(valgtBord))
                brugteBorde.add(valgtBord)
                valgtBord
            } else {
                null // Takeaway
            }
            
            val ordre = repository.createOrder(
                items = items,
                bordNr = bordNr,
                bemærkninger = ""
            )
            
            repository.updateOrderStatus(ordre.id, OrdreStatusType.I_GANG)
        }
        
        // 3 ordrer "Klar til afhentning"
        repeat(3) {
            val items = generateRandomItems(count = Random.nextInt(1, 3))
            
            // Vælg et bord der IKKE allerede har en ordre
            val bordNr = if (Random.nextDouble() < 0.7) {
                var valgtBord: Int
                do {
                    valgtBord = Random.nextInt(1, 21)
                } while (brugteBorde.contains(valgtBord))
                brugteBorde.add(valgtBord)
                valgtBord
            } else {
                null // Takeaway
            }
            
            val ordre = repository.createOrder(
                items = items,
                bordNr = bordNr,
                bemærkninger = ""
            )
            
            repository.updateOrderStatus(ordre.id, OrdreStatusType.KLAR)
        }
    }
    
    /**
     * Generer tilfældige ordre items
     */
    private fun generateRandomItems(count: Int): List<KurvItem> {
        val selectedProducts = produkter.shuffled().take(count)
        return selectedProducts.map { produkt ->
            KurvItem(
                produkt = produkt,
                antal = Random.nextInt(1, 4)
            )
        }
    }
    
    /**
     * Ryd alle data (til testing)
     */
    fun clear(repository: OrderRepository) {
        repository._ordrer.value = emptyList()
        repository._transaktioner.value = emptyList()
        repository._borde.value = (1..20).map { Bord(nummer = it) }
        println("🧹 DataSeeder: Alle data ryddet")
    }
}
