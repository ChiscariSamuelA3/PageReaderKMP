package com.demo.pagereader.api

import com.demo.pagereader.api.endpoints.PageReaderApi
import com.demo.pagereader.repositories.PageRepository
import com.demo.pagereader.repositories.PageRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent

internal object DependencyContainer {
    private val httpClient by lazy { PageReaderApiClient.create() }

    val pageRepository: PageRepository by lazy {
        PageRepositoryImpl(PageReaderApi(httpClient))
    }
}

internal object PageReaderApiClient {

    // OkHttp / Darwin
    fun create(engine: HttpClientEngine? = null): HttpClient =
        if (engine == null) HttpClient { configure() } else HttpClient(engine) { configure() }

    private fun HttpClientConfig<*>.configure() {
        expectSuccess = false

        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 15_000
        }
        install(HttpRedirect) {
            allowHttpsDowngrade = false
        }
        install(UserAgent) {
            agent = "PageReader/1.0"
        }
    }
}