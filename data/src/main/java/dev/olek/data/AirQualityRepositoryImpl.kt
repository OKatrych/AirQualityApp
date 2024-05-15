package dev.olek.data

import dev.olek.data.local.AirQualityDatabase
import dev.olek.data.remote.AirQualityApi
import dev.olek.data.remote.AirQualityResponseMapper
import dev.olek.domain.AirQualityRepository
import dev.olek.domain.models.AirQualityReport
import dev.olek.domain.models.Location
import org.koin.core.annotation.Single

@Single
internal class AirQualityRepositoryImpl(
    private val api: AirQualityApi,
    private val database: AirQualityDatabase,
    private val airQualityResponseMapper: AirQualityResponseMapper,
): AirQualityRepository {

    override suspend fun getLatestAirQualityReportFor(
        useCache: Boolean,
        location: Location,
    ): AirQualityReport {
        if (useCache) {
            val cachedReport = database.getLatestAirQualityReportFor(location)
            if (cachedReport != null) {
                return cachedReport
            }
        }

        val recentReports: List<AirQualityReport> = api.getRecentAirQualityReportsFor(location)
            .let(airQualityResponseMapper::map)
        if (recentReports.isEmpty()) {
            throw NoSuchElementException("There is no recent air quality report for $location")
        }
        return recentReports.first().also {
            database.insertAirQualityReport(it)
        }
    }
}