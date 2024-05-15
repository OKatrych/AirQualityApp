package dev.olek.domain

import dev.olek.domain.models.AirQualityReport
import dev.olek.domain.models.Location

interface AirQualityRepository {

    suspend fun getLatestAirQualityReportFor(
        useCache: Boolean,
        location: Location
    ): AirQualityReport
}