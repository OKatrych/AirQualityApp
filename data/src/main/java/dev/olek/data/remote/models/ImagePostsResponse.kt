package dev.olek.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ImagePostsResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<ImagePostResponse>
)

@Serializable
data class ImagePostResponse(
    val id: Long,
    val pageURL: String,
    val tags: String,
    val previewURL: String,
    val largeImageURL: String,
    val downloads: Int,
    val likes: Int,
    val comments: Int,
    val user: String,
)