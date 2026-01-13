package dev.abhinav.stocktracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = YellowGreen,
    onPrimary = Color.Black,
    primaryContainer = YellowGreen.copy(alpha = 0.1f),
    onPrimaryContainer = Color.Black,

    secondary = GreenPositive,
    onSecondary = Color.White,
    secondaryContainer = GreenPositive.copy(alpha = 0.1f),
    onSecondaryContainer = GreenPositive,

    tertiary = RedNegative,
    onTertiary = Color.White,
    tertiaryContainer = RedNegative.copy(alpha = 0.1f),
    onTertiaryContainer = RedNegative,

    error = RedNegative,
    onError = Color.White,
    errorContainer = RedNegative.copy(alpha = 0.1f),
    onErrorContainer = RedNegative,

    background = LightBackground,
    onBackground = LightTextPrimary,

    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightCardBackground,
    onSurfaceVariant = LightTextSecondary,

    outline = LightBorder,
    outlineVariant = LightBorder.copy(alpha = 0.5f),

    inverseSurface = DarkSurface,
    inverseOnSurface = DarkTextPrimary,
    inversePrimary = DarkYellowGreen
)

// ========== DARK COLOR SCHEME ==========
private val DarkColorScheme = darkColorScheme(
    primary = DarkYellowGreen,
    onPrimary = Color.Black,
    primaryContainer = DarkYellowGreen.copy(alpha = 0.15f),
    onPrimaryContainer = DarkYellowGreen,

    secondary = GreenPositive,
    onSecondary = Color.White,
    secondaryContainer = GreenPositive.copy(alpha = 0.15f),
    onSecondaryContainer = GreenPositive,

    tertiary = RedNegative,
    onTertiary = Color.White,
    tertiaryContainer = RedNegative.copy(alpha = 0.15f),
    onTertiaryContainer = RedNegative,

    error = RedNegative,
    onError = Color.White,
    errorContainer = RedNegative.copy(alpha = 0.15f),
    onErrorContainer = RedNegative,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCardBackground,
    onSurfaceVariant = DarkTextSecondary,

    outline = DarkBorder,
    outlineVariant = DarkBorder.copy(alpha = 0.5f),

    inverseSurface = LightSurface,
    inverseOnSurface = LightTextPrimary,
    inversePrimary = YellowGreen
)

@Composable
fun StockTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}