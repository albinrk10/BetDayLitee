package com.albin.betdaylite.core.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val BetDayShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

val CardShape = RoundedCornerShape(12.dp)
val OddsButtonShape = RoundedCornerShape(10.dp)
val PillShape = RoundedCornerShape(50)
val SheetTopShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
