package com.demo.pagereader.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.demo.pagereader.R
import com.demo.pagereader.api.PageReaderError

@Composable
fun errorMessageMapper(error: PageReaderError): String = when (error) {
    PageReaderError.InvalidUrl -> stringResource(R.string.error_invalid_url)
    PageReaderError.InsecureUrl -> stringResource(R.string.error_insecure_url)
    PageReaderError.InvalidSelector -> stringResource(R.string.error_invalid_selector)
    is PageReaderError.Network -> stringResource(R.string.error_network, error.reason)
    is PageReaderError.Http -> stringResource(R.string.error_http, error.code)
    PageReaderError.ResponseTooLarge -> stringResource(R.string.error_response_too_large)
    is PageReaderError.NothingFound -> stringResource(R.string.error_nothing_found, error.selector)
}