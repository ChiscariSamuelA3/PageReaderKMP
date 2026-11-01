package com.demo.pagereader.util

import com.demo.pagereader.api.endpoints.PageReaderException
import com.demo.pagereader.models.PageReaderError
import io.ktor.http.URLProtocol
import io.ktor.http.parseUrl

internal object UrlNormalizer {

    fun normalize(input: String): String {
        val trimmed = input.trim()

        if (trimmed.startsWith("http://", ignoreCase = true)) {
            throw PageReaderException(PageReaderError.InsecureUrl)
        }

        if (!trimmed.startsWith("https://", ignoreCase = true)) {
            throw PageReaderException(PageReaderError.InvalidUrl)
        }

        val url = parseUrl(trimmed)

        if (url == null || url.protocol != URLProtocol.HTTPS || url.host.isBlank() || ' ' in trimmed) {
            throw PageReaderException(PageReaderError.InvalidUrl)
        }
        return url.toString()
    }
}
