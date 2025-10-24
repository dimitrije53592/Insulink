package com.dj.insulink.core.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    // padding
    val commonPadding48: Dp = 48.dp,
    val commonPadding40: Dp = 40.dp,
    val commonPadding32: Dp = 32.dp,
    val commonPadding24: Dp = 24.dp,
    val commonPadding20: Dp = 20.dp,
    val commonPadding16: Dp = 16.dp,
    val commonPadding12: Dp = 12.dp,
    val commonPadding8: Dp = 8.dp,
    val commonPadding4: Dp = 4.dp,

    // spacing
    val commonSpacing80: Dp = 80.dp,
    val commonSpacing64: Dp = 64.dp,
    val commonSpacing32: Dp = 32.dp,
    val commonSpacing24: Dp = 24.dp,
    val commonSpacing20: Dp = 20.dp,
    val commonSpacing16: Dp = 16.dp,
    val commonSpacing12: Dp = 12.dp,
    val commonSpacing8: Dp = 8.dp,
    val commonSpacing4: Dp = 4.dp,

    // icon sizes
    val registrationMainIconSize: Dp = 100.dp,
    val reminderIconSize: Dp = 80.dp,
    val sideDrawerIconSize: Dp = 48.dp,
    val buttonIconSize: Dp = 34.dp,
    val textFieldIconSize: Dp = 24.dp,

    // buttons - dimensions
    val commonButtonHeight50: Dp = 50.dp,
    val commonProgressIndicatorSize25: Dp = 25.dp,

    // buttons - radius
    val commonButtonRadius12: Dp = 12.dp,
    val commonButtonRadius8: Dp = 8.dp,
    val commonButtonRadius4: Dp = 4.dp,

    // buttons - border
    val commonButtonBorder1: Dp = 1.dp,

    // other
    val commonElevation0: Dp = 0.dp,
    val commonElevation2: Dp = 2.dp
)

val LocalAppDimensions = compositionLocalOf { Dimensions() }