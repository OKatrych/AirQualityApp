package dev.olek.data

import dev.olek.data.local.ImagePostsDatabase
import dev.olek.data.remote.ImagePostApi
import dev.olek.data.remote.ImagePostsResponseMapper
import dev.olek.domain.ImagePostRepository
import dev.olek.domain.models.ImagePost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
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
        private const val RETRIES_DELAY = 3L
    }

    override fun observeImagePosts(query: String): Flow<List<ImagePost>> = flow {
        val networkScope = CoroutineScope(currentCoroutineContext())

        // Try to load the newest images from the network and save them to local storage
        // This is done asynchronously to allow retries on network issues
        getImagePostsFromRemoteSource(query).onEach {
            database.insertImagePosts(query, it)
        }.launchIn(networkScope)

        emitAll(database.observeImagePosts(query))
    }

    private fun getImagePostsFromRemoteSource(query: String): Flow<List<ImagePost>> = flow {
        emit(imagePostsResponseMapper.map(api.getImagePosts(query)))
    }.retryWhen { cause, attempt ->
        if (attempt < MAX_RETRIES && cause is IOException) { // Retry only when connection issue occurred
            delay(RETRIES_DELAY)
            true
        } else {
            false
        }
    }
}