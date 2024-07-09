package dev.olek.data

import dev.olek.data.local.ImagePostsDatabase
import dev.olek.data.remote.ImagePostApi
import dev.olek.data.remote.ImagePostsResponseMapper
import dev.olek.domain.ImagePostRepository
import dev.olek.domain.models.ImagePost
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.koin.core.annotation.Single
import java.io.IOException

@Single
internal class ImagePostRepositoryImpl(
    private val api: ImagePostApi,
    private val database: ImagePostsDatabase,
    private val imagePostsResponseMapper: ImagePostsResponseMapper,
) : ImagePostRepository {

    companion object {
        private const val MAX_RETRIES = 3
        private const val RETRIES_DELAY_MS = 3000L
    }

    override fun observeImagePosts(query: String): Flow<List<ImagePost>> = merge(
        database.observeImagePosts(query),
        getImagePostsFromRemoteSource(query)
    )

    override suspend fun getImagePost(id: Long): ImagePost = flow {
        val postList = imagePostsResponseMapper.map(api.getImagePost(id))
        emit(postList.first())
    }.retry().first()

    private fun getImagePostsFromRemoteSource(query: String): Flow<List<ImagePost>> = flow {
        emit(imagePostsResponseMapper.map(api.getImagePosts(query)))
    }.retry().onEach { database.insertImagePosts(query, it) }

    private fun <T> Flow<T>.retry() = this.retryWhen { cause, attempt ->
        if (attempt < MAX_RETRIES && cause is IOException) { // Retry only when connection issue occurred
            delay(RETRIES_DELAY_MS)
            true
        } else {
            false
        }
    }
}