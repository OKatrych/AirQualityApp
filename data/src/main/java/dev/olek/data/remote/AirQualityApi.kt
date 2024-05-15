package dev.olek.data.remote

import dev.olek.data.remote.models.AirQualityResponse
import dev.olek.domain.models.Location
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
internal class AirQualityApi(
    private val client: HttpClient,
) {

    companion object {
        private const val API_KEY = "USE_YOUR"
    }

    suspend fun getRecentAirQualityReportsFor(
        location: Location,
    ): AirQualityResponse {
        val url = "air_pollution?lat=${location.latitude}&lon=${location.longitude}&appid=$API_KEY"
        return client.get(url).body()
    }
}