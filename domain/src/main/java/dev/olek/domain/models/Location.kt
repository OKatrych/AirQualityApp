package dev.olek.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(val latitude: Double, val longitude: Double)
