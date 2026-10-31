package com.demo.pagereader.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PageReaderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            darkColorScheme(
                primary = Color(0xFF6366F1),
                onPrimary = Color.White,
                background = Color.Black,
                surface = Color(0xFF1C1C1E),
                outline = Color(0xFF48484A),
                error = Color(0xFFFF453A),
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF4F46E5),
                onPrimary = Color.White,
                background = Color(0xFFF2F2F7),
                surface = Color.White,
                outline = Color(0xFFC7C7CC),
                error = Color(0xFFFF3B30),
            )
        },
        content = content,
    )
}