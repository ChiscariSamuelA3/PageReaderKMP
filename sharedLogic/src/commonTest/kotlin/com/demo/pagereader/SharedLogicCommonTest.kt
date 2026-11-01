package com.demo.pagereader

import com.demo.pagereader.api.endpoints.PageReaderException
import com.demo.pagereader.models.PageReaderError
import com.demo.pagereader.util.HtmlParser
import com.demo.pagereader.util.UrlNormalizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SharedLogicCommonTest {

    private val html = """
        <html>
          <head><title>Example Domain</title></head>
          <body><h1>Hello <b>world</b></h1><p>First</p><p>Second</p></body>
        </html>
    """.trimIndent()

    @Test
    fun extractsTitle() {
        assertEquals("Example Domain", HtmlParser.extractText(html, "title"))
    }

    @Test
    fun extractsH1() {
        assertEquals("Hello world", HtmlParser.extractText(html, "h1"))
    }

    @Test
    fun throwsNothingFound() {
        val error = assertFailsWith<PageReaderException> { HtmlParser.extractText(html, "h2") }
        assertEquals(PageReaderError.NothingFound("h2"), error.error)
    }

    @Test
    fun throwsInvalidSelector() {
        listOf("", "h1[", "div >>").forEach { selector ->
            val error = assertFailsWith<PageReaderException>(selector) {
                HtmlParser.extractText(
                    html,
                    selector
                )
            }
            assertEquals(PageReaderError.InvalidSelector, error.error, selector)
        }
    }

    @Test
    fun keepsHttpsUrl() {
        assertEquals(
            "https://example.com/path?q=1",
            UrlNormalizer.normalize("https://example.com/path?q=1")
        )
    }

    @Test
    fun rejectsHttp() {
        val error =
            assertFailsWith<PageReaderException> { UrlNormalizer.normalize("http://example.com") }
        assertEquals(PageReaderError.InsecureUrl, error.error)
    }
}