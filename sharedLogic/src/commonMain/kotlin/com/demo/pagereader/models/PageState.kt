package com.demo.pagereader.models

import com.demo.pagereader.ui.ReadStatus

data class PageState(
    val url: String = "https://example.com",
    val selector: String = "title",
    val status: ReadStatus = ReadStatus.Idle,
)