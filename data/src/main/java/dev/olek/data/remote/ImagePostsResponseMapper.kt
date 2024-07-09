package dev.olek.data.remote

import dev.olek.data.remote.models.ImagePostsResponse
import dev.olek.domain.models.ImagePost
import org.koin.core.annotation.Factory

@Factory
internal class ImagePostsResponseMapper {

    fun map(response: ImagePostsResponse): List<ImagePost> {
        return response.hits.map { responseItem ->
            ImagePost(
                id = responseItem.id,
                tags = responseItem.tags.split(", "),
                previewUrl = responseItem.previewURL,
                largeImageUrl = responseItem.largeImageURL,
                downloadsCount = responseItem.downloads,
                likesCount = responseItem.likes,
                commentsCount = responseItem.comments,
                userName = responseItem.user,
            )
        }
    }
}
