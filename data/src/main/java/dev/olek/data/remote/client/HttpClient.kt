package dev.olek.data.remote.client

import dev.olek.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

@Factory
internal fun getDefaultHttpClient(
    isDebug: Boolean = BuildConfig.DEBUG,
): HttpClient =
    HttpClient(OkHttp) {
        developmentMode = isDebug

        install(Logging) {
            level = if (isDebug) LogLevel.BODY else LogLevel.NONE
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.i(message, tag = "Ktor")
                }
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }

        install(DefaultRequest) {
            url(BASE_URL)
        }
    }
