package com.example.app_uikit.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = colorBlueLight,
    onPrimary = colorWhiteLight,
    secondary = colorGrayLight,
    onSecondary = colorWhiteLight,
    secondaryContainer = colorGrayLightDark,
    tertiary = colorGreenLight,
    onTertiary = colorWhiteLight,
    background = backPrimaryLight,
    onBackground = labelPrimaryLight,
    surface = backElevatedLight,
    onSurface = labelPrimaryLight,
    surfaceVariant = backSecondaryLight,
    onSurfaceVariant = labelSecondaryLight,
    error = colorRedLight,
    onError = colorWhiteLight,
    outline = supportSeparatorLight,
    inverseSurface = supportOverlayLight,
    surfaceDim = labelDisableLight,
    surfaceTint = labelTertiaryLight
)

private val LightColorScheme = lightColorScheme(
    primary = colorBlueDark,
    onPrimary = colorWhiteDark,
    secondary = colorGrayDark,
    secondaryContainer = colorGrayLightLight,
    onSecondary = colorWhiteDark,
    tertiary = colorGreenDark,
    onTertiary = colorWhiteDark,
    background = backPrimaryDark,
    onBackground = labelPrimaryDark,
    surface = backElevatedDark,
    onSurface = labelPrimaryDark,
    surfaceVariant = backSecondaryDark,
    onSurfaceVariant = labelSecondaryDark,
    error = colorRedDark,
    onError = colorWhiteDark,
    outline = supportSeparatorDark,
    inverseSurface = supportOverlayDark,
    surfaceDim = labelDisableDark,
    surfaceTint = labelTertiaryDark
)

@Immutable
data class CustomColorsPalette(
    val support_separatord: Color = Color.Unspecified,
    val support_overlay: Color = Color.Unspecified,

    val label_primary: Color = Color.Unspecified,
    val label_secondary: Color = Color.Unspecified,
    val label_tertiary: Color = Color.Unspecified,
    val label_disable: Color = Color.Unspecified,

    val color_red: Color = Color.Unspecified,
    val color_green: Color = Color.Unspecified,
    val color_blue: Color = Color.Unspecified,
    val color_gray: Color = Color.Unspecified,
    val color_gray_light: Color = Color.Unspecified,
    val color_white: Color = Color.Unspecified,

    val back_primary: Color = Color.Unspecified,
    val back_secondary: Color = Color.Unspecified,
    val back_elevated: Color = Color.Unspecified,
)

val OnLightCustomColorsPalette = CustomColorsPalette(
    support_separatord = supportSeparatorLight,
    support_overlay = supportOverlayLight,

    label_primary = labelPrimaryLight,
    label_secondary = labelSecondaryLight,
    label_tertiary = labelTertiaryLight,
    label_disable = labelDisableLight,

    color_red = colorRedLight,
    color_green = colorGreenLight,
    color_blue = colorBlueLight,
    color_gray = colorGrayLight,
    color_gray_light = colorGrayLightLight,
    color_white = colorWhiteLight,

    back_primary = backPrimaryLight,
    back_secondary = backSecondaryLight,
    back_elevated = backElevatedLight,
)

val OnDarkCustomColorsPalette  = CustomColorsPalette(
    support_separatord = supportSeparatorDark,
    support_overlay = supportOverlayDark,

    label_primary = labelPrimaryDark,
    label_secondary = labelSecondaryDark,
    label_tertiary = labelTertiaryDark,
    label_disable = labelDisableDark,

    color_red = colorRedDark,
    color_green = colorGreenDark,
    color_blue = colorBlueDark,
    color_gray = colorGrayDark,
    color_gray_light = colorGrayLightDark,
    color_white = colorWhiteDark,

    back_primary = backPrimaryDark,
    back_secondary = backSecondaryDark,
    back_elevated = backElevatedDark,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }

val MaterialTheme.customColorsPalette: CustomColorsPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorsPalette.current

@Composable
fun ToDoListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val customColorsPalette =
        if (darkTheme) OnDarkCustomColorsPalette
        else OnLightCustomColorsPalette

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalCustomColorsPalette provides customColorsPalette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object ToDoListTheme {
    /**
     * Retrieves the current [ColorScheme] at the call site's position in the hierarchy.
     */
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    /**
     * Retrieves the current [Typography] at the call site's position in the hierarchy.
     */
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val customColorsPalette: CustomColorsPalette
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.customColorsPalette
}
