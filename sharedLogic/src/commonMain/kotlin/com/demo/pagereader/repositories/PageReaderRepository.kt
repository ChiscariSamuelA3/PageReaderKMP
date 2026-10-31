package com.demo.pagereader.repositories

import com.demo.pagereader.api.endpoints.PageReaderApi
import com.demo.pagereader.util.HtmlParser
import com.demo.pagereader.util.UrlNormalizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface PageRepository {
    suspend fun readText(url: String, selector: String): String
}

internal class PageRepositoryImpl(
    private val pageReaderApi: PageReaderApi
) : PageRepository {

    override suspend fun readText(url: String, selector: String): String {
        val normalizedUrl = UrlNormalizer.normalize(url)
        val html = pageReaderApi.fetchHtml(normalizedUrl)

        return withContext(Dispatchers.Default) {
            HtmlParser.extractText(html, selector)
        }
    }
}