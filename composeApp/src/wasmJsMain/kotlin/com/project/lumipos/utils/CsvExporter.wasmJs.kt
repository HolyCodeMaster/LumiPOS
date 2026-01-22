package com.project.lumipos.utils

/**
 * Wasm-specific implementation for CSV download
 * Note: Rigtig file download kræver JS interop som ikke er fuldt understøttet i Kotlin Wasm endnu.
 * For nu logger vi CSV indholdet til konsollen.
 */
actual fun downloadCsv(content: String, filename: String) {
    println("=== CSV EXPORT ===")
    println("Filename: $filename")
    println(content)
    println("=== END CSV ===")
    println("✅ CSV data logget til konsol (åbn browser DevTools for at se)")
    println("💡 Tip: Kopier data fra konsol og gem som .csv fil manuelt")
}
