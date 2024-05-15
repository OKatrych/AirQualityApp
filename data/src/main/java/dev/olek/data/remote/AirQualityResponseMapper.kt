package dev.olek.data.remote

import dev.olek.data.remote.models.AirQualityResponse
import dev.olek.domain.models.AirQualityReport
import dev.olek.domain.models.Location
import org.koin.core.annotation.Factory

@Factory
internal class AirQualityResponseMapper {

    fun map(response: AirQualityResponse): List<AirQualityReport> {
        return response.list.map { report ->
            AirQualityReport(
                location = Location(latitude = response.coord.lat, longitude = response.coord.lon),
                qualityIndex = report.main.aqi,
                co = report.components.co,
                no = report.components.no,
                no2 = report.components.no2,
                o3 = report.components.o3,
                so2 = report.components.so2,
                pm2_5 = report.components.pm2_5,
                pm10 = report.components.pm10,
                nh3 = report.components.nh3,
            )
        }
    }
}