package com.demo.pagereader.util

import com.demo.pagereader.api.endpoints.PageReaderException
import com.demo.pagereader.models.PageReaderError
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.select.Selector

internal object HtmlParser {

    const val MAX_EXTRACTED_TEXT_LENGTH = 10_000

    fun extractText(html: String, selector: String): String {
        val query = selector.trim()
        if (query.isEmpty()) throw PageReaderException(PageReaderError.InvalidSelector)

        val elements = try {
            Ksoup.parse(html).select(query)
        } catch (e: Selector.SelectorParseException) {
            throw PageReaderException(PageReaderError.InvalidSelector)
        }

        val text = elements.eachText().joinToString(separator = "\n")
        if (text.isBlank()) throw PageReaderException(PageReaderError.NothingFound(query))

        return text.take(MAX_EXTRACTED_TEXT_LENGTH)
    }
}
