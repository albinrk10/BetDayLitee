package com.albin.betdaylite.core.constants

import java.text.NumberFormat
import java.util.Locale

object BetConstants {
    const val INITIAL_WALLET_BALANCE = 1250.00
    
    val QUICK_STAKES = listOf(10.0, 25.0, 50.0, 100.0)
    
    val POPULAR_LEAGUES = listOf(
        "Todas",
        "Premier League",
        "LaLiga",
        "Champions League",
        "Serie A"
    )

    fun formatCurrency(amount: Double): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        return "S/ ${numberFormat.format(amount)}"
    }

    fun formatOdd(odd: Double): String {
        return String.format(Locale.US, "%.2f", odd)
    }
}
