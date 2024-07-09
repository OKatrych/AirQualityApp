package dev.olek.domain

import dev.olek.domain.models.ImagePost
import kotlinx.coroutines.flow.Flow

interface ImagePostRepository {

    fun observeImagePosts(query: String): Flow<List<ImagePost>> // TODO add pagination support
    
}