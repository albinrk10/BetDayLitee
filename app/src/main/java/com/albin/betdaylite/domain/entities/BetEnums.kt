package com.albin.betdaylite.domain.entities

enum class BetPick(val symbol: String, val label: String) {
    HOME("1", "Local"),
    DRAW("X", "Empate"),
    AWAY("2", "Visitante");

    companion object {
        fun fromString(value: String): BetPick {
            return when (value.uppercase()) {
                "HOME", "1" -> HOME
                "DRAW", "X" -> DRAW
                "AWAY", "2" -> AWAY
                else -> HOME
            }
        }
    }
}

enum class BetStatus(val label: String) {
    PENDING("Pendiente"),
    WON("Ganada"),
    LOST("Perdida");

    companion object {
        fun fromString(value: String): BetStatus {
            return when (value.uppercase()) {
                "WON" -> WON
                "LOST" -> LOST
                else -> PENDING
            }
        }
    }
}
