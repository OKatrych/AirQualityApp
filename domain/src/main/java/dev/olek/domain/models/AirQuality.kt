package dev.olek.domain.models

import kotlinx.serialization.Serializable

/**
 * @param location Location of the report
 * @param qualityIndex Air Quality Index, possible values: 1, 2, 3, 4, 5.
 *                     Where 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor
 * @param co concentration of CO (Carbon monoxide), μg/m3
 * @param no concentration of NO (Nitrogen monoxide), μg/m3
 * @param no2 concentration of NO2 (Nitrogen dioxide), μg/m3
 * @param o3 concentration of O3 (Ozone), μg/m3
 * @param pm2_5 concentration of PM2.5 (Fine particles matter), μg/m3
 * @param pm10 concentration of PM10 (Coarse particulate matter), μg/m3
 * @param nh3 concentration of NH3 (Ammonia), μg/m3
 *
 */
@Serializable
data class AirQualityReport(
    val location: Location,
    val qualityIndex: Int,
    val co: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm2_5: Double,
    val pm10: Double,
    val nh3: Double,
)
