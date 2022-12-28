package com.unbiasedshelf.agepredictor.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ColorPalette = lightColors(
    primary = PrimaryGray,
    onPrimary = onPrimaryGray,
    secondary = AccentGreen,
    onSecondary = BGBlue,
    surface = SurfaceGray,
    onSurface = OnSurfaceBlue,
    background = White,
    onBackground = BGBlue
)

@Composable
fun AgePredictorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}