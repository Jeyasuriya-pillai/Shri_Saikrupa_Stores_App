package com.shrisaikrupa.stores.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = White,
    secondary = Gold,
    onSecondary = DarkGray,
    background = LightGray,
    surface = White,
    onBackground = DarkGray,
    onSurface = DarkGray,
)

@Composable
fun ShriSaikrupaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}