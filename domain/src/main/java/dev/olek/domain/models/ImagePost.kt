package dev.olek.domain.models

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class ImagePost(
    val id: Long,
    val tags: List<String>,
    val previewUrl: String,
    val largeImageUrl: String,
    val downloadsCount: Int,
    val likesCount: Int,
    val commentsCount: Int,
    val userName: String,
)