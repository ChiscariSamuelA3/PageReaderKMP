package com.demo.pagereader.api

sealed interface PageReaderError {
    data object InvalidUrl : PageReaderError
    data object InsecureUrl : PageReaderError
    data object InvalidSelector : PageReaderError
    data class Network(val reason: String) : PageReaderError
    data class Http(val code: Int) : PageReaderError
    data object ResponseTooLarge : PageReaderError
    data class NothingFound(val selector: String) : PageReaderError
}