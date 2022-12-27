package com.unbiasedshelf.agepredictor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// todo dark theme?
private val DarkColorPalette = darkColors(
    primary = AccentGreen,
    onPrimary = BGBlue,
    surface = SurfaceGray,
    onSurface = OnSurfaceBlue
)

private val LightColorPalette = lightColors(
    primary = AccentGreen,
    onPrimary = BGBlue,
    surface = SurfaceGray,
    onSurface = OnSurfaceBlue
)

@Composable
fun AgePredictorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}