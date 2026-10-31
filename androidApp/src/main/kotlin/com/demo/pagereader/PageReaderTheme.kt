package com.demo.pagereader

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PageReaderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF4F46E5),
            onPrimary = Color.White,
            background = Color(0xFFF2F2F7),
            surface = Color.White,
            outline = Color(0xFFC7C7CC),
            error = Color(0xFFFF3B30),
        ),
        content = content,
    )
}