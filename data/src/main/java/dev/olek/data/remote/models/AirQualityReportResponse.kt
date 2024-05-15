package dev.olek.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
internal data class AirQualityResponse(
    val coord: Location,
    val list: List<AirQualityReport>,
)

@Serializable
internal data class Location(val lon: Double, val lat: Double)

@Serializable
internal data class AirQualityReport(
    val dt: Long,
    val main: AirQualityMain,
    val components: AirQualityComponents,
)

@Serializable
internal data class AirQualityMain(
    val aqi: Int,
)

@Serializable
internal data class AirQualityComponents(
    val co: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm2_5: Double,
    val pm10: Double,
    val nh3: Double,
)

/*
{
  "coord":[
    50,
    50
  ],
  "list":[
    {
      "dt":1605182400,
      "main":{
        "aqi":1
      },
      "components":{
        "co":201.94053649902344,
        "no":0.01877197064459324,
        "no2":0.7711350917816162,
        "o3":68.66455078125,
        "so2":0.6407499313354492,
        "pm2_5":0.5,
        "pm10":0.540438711643219,
        "nh3":0.12369127571582794
      }
    }
  ]
}
 */