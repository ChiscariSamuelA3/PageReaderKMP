package com.demo.pagereader.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.demo.pagereader.theme.PageReaderTheme
import com.demo.pagereader.ui.screens.PageReaderScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            PageReaderTheme {
                PageReaderScreen()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    PageReaderTheme {
        PageReaderScreen()
    }
}