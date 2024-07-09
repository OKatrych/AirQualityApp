package dev.olek.data.remote

import dev.olek.data.remote.models.ImagePostsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
internal class ImagePostApi(
    private val client: HttpClient,
) {

    companion object {
        private const val API_KEY = "24277301-367414e95fcae46ab3c0252b7" // FIXME Usually this should be stored in secure place (prefferably in backend after sign in)
        private const val QUERY_MAX_SIZE = 100
    }

    suspend fun getImagePosts(
        query: String,
        page: Int = 1,
        perPage: Int = 30,
    ): ImagePostsResponse {
        val normalizedQuery = query.replace(' ', '+').take(QUERY_MAX_SIZE)
        val url = "?key=$API_KEY&q=$normalizedQuery&image_type=photo&page=$page&per_page=$perPage"
        return client.get(url).body()
    }
}