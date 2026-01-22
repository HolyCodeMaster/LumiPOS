package com.project.lumipos.utils

import com.project.lumipos.model.XRapport

/**
 * CSV Exporter utility
 * Genererer CSV indhold fra X-Rapport data
 */
object CsvExporter {
    
    /**
     * Generer CSV fra X-Rapport
     */
    fun generateXRapportCsv(rapport: XRapport): String {
        val csv = StringBuilder()
        
        // Header
        csv.appendLine("LumiPOS - X-Rapport")
        csv.appendLine("Dato:,21. Januar 2026")
        csv.appendLine("Tidspunkt:,12:45")
        csv.appendLine()
        
        // Omsætning sektion
        csv.appendLine("=== OMSÆTNING ===")
        csv.appendLine("Beskrivelse,Værdi")
        csv.appendLine("Antal ordrer,${rapport.statistik.antalOrdrer}")
        csv.appendLine("Total omsætning,${rapport.statistik.omsætning} DKK")
        csv.appendLine("Heraf moms (25%),${rapport.statistik.moms} DKK")
        csv.appendLine("Netto omsætning,${rapport.statistik.netto} DKK")
        csv.appendLine()
        
        // Top produkter sektion
        csv.appendLine("=== TOP 5 PRODUKTER ===")
        csv.appendLine("Rank,Produkt,Antal")
        rapport.statistik.topProdukter.forEachIndexed { index, (produkt, antal) ->
            csv.appendLine("${index + 1},$produkt,$antal")
        }
        csv.appendLine()
        
        // Footer
        csv.appendLine("=== SYSTEM INFO ===")
        csv.appendLine("Genereret af,LumiPOS")
        csv.appendLine("Version,1.0.0")
        
        return csv.toString()
    }
    
    /**
     * Generer filnavn for X-Rapport
     */
    fun generateXRapportFilename(): String {
        return "x_rapport_2026-01-21_1245.csv"
    }
    
}

/**
 * Download CSV fil (denne funktion er platform-specifik)
 * I Wasm bruges browser's download API
 */
expect fun downloadCsv(content: String, filename: String)
