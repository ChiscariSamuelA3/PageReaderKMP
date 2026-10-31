package com.demo.pagereader.api.endpoints

import com.demo.pagereader.models.PageReaderError
import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.charset
import io.ktor.http.isSuccess
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.readText
import io.ktor.utils.io.exhausted
import io.ktor.utils.io.readBuffer
import kotlinx.io.IOException

internal class PageReaderException(val error: PageReaderError) : Exception(error.toString())

internal class PageReaderApi(private val client: HttpClient) {
    suspend fun fetchHtml(url: String): String = try {
        client.prepareGet(url).execute { response ->
            if (!response.status.isSuccess()) {
                throw PageReaderException(PageReaderError.Http(response.status.value))
            }
            response.readPage()
        }
    } catch (e: IOException) {
        // Timeouts, DNS, TLS, connection errors
        throw PageReaderException(PageReaderError.Network(e.message ?: "Unknown network error"))
    }

    private suspend fun HttpResponse.readPage(): String {
        val channel = bodyAsChannel()
        val page = channel.readBuffer(MAX_PAGE_SIZE)
        if (!channel.exhausted()) {
            throw PageReaderException(PageReaderError.ResponseTooLarge)
        }
        return page.readText(charset() ?: Charsets.UTF_8)
    }

    companion object {
        const val MAX_PAGE_SIZE = 2L * 1024 * 1024 // 2 MB
    }
}