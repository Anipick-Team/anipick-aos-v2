package com.jparkbro.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AniPickColors(
    val primary: Color,
    val point: Color,
    val black: Color,
    val white: Color,
    val lightGray: Color,
    val gray: Color,
    val backgroundGray: Color,
    val textGray: Color,
    val dimmed: Color,
    val primary10: Color,
)

private val LightAniPickColors = AniPickColors(
    primary = lightPrimary,
    point = lightPoint,
    black = lightBlack,
    white = lightWhite,
    lightGray = lightGray,
    gray = lightGray2,
    backgroundGray = lightBackgroundGray,
    textGray = lightTextGray,
    dimmed = lightDimmed,
    primary10 = lightPrimary10,
)

private val DarkAniPickColors = AniPickColors(
    primary = darkPrimary,
    point = darkPoint,
    black = darkBlack,
    white = darkWhite,
    lightGray = darkGray,
    gray = darkGray2,
    backgroundGray = darkBackgroundGray,
    textGray = darkTextGray,
    dimmed = darkDimmed,
    primary10 = darkPrimary10,
)

private val LocalAniPickColors = staticCompositionLocalOf { LightAniPickColors }

@Composable
fun AniPick_v2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val aniPickColors = if (darkTheme) DarkAniPickColors else LightAniPickColors

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = aniPickColors.primary,
            secondary = aniPickColors.point,
            background = aniPickColors.white,
            surface = aniPickColors.white,
            onBackground = aniPickColors.black,
            onSurface = aniPickColors.black,
        )
    } else {
        lightColorScheme(
            primary = aniPickColors.primary,
            secondary = aniPickColors.point,
            background = aniPickColors.white,
            surface = aniPickColors.white,
            onBackground = aniPickColors.black,
            onSurface = aniPickColors.black,
        )
    }

    CompositionLocalProvider(LocalAniPickColors provides aniPickColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object AniPickTheme {
    val colors: AniPickColors
        @Composable
        get() = LocalAniPickColors.current

    val typography: AniPickTypographyStyle
        get() = AniPickTypography
}
