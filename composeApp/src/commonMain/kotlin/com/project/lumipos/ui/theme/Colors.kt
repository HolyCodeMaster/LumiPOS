package com.project.lumipos.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// LumiPOS farvepalette
object LumiPOSColors {
    val Baggrund = Color(0xFF1A1D21)
    val Panel = Color(0xFF25282C)
    val AccentBlå = Color(0xFF3B82F6)
    val TekstLys = Color(0xFFF9FAFB)
    val TekstDæmpet = Color(0xFF9CA3AF)
    val SurfaceVariant = Color(0xFF32353A)
    
    val Orange = Color(0xFFF97316)
    val Grøn = Color(0xFF22C55E)
    val Rød = Color(0xFFEF4444)
    val Lilla = Color(0xFF6366F1)
}

// Dark theme colorscheme
fun lumiPOSDarkColorScheme() = darkColorScheme(
    primary = LumiPOSColors.AccentBlå,
    onPrimary = Color.White,
    background = LumiPOSColors.Baggrund,
    surface = LumiPOSColors.Panel,
    onBackground = LumiPOSColors.TekstLys,
    onSurface = LumiPOSColors.TekstLys,
    surfaceVariant = LumiPOSColors.SurfaceVariant,
    onSurfaceVariant = LumiPOSColors.TekstDæmpet
)
