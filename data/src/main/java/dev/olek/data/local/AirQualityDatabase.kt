package dev.olek.data.local

import dev.olek.domain.models.AirQualityReport
import dev.olek.domain.models.Location
import org.koin.core.annotation.Single

@Single
internal class AirQualityDatabase {

    suspend fun getLatestAirQualityReportFor(
        location: Location,
    ): AirQualityReport? {
        // TODO implement real DB
        return null
    }

    suspend fun insertAirQualityReport(airQualityReport: AirQualityReport) {
        // TODO implement real DB
    }
}