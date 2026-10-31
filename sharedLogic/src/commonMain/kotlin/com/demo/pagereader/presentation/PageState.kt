package com.demo.pagereader.presentation

data class PageState(
    val url: String = "https://example.com",
    val selector: String = "title",
    val status: ReadStatus = ReadStatus.Idle,
)