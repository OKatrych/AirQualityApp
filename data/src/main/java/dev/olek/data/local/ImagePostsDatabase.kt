package dev.olek.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.olek.domain.models.ImagePost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class ImagePostsDatabase(
    private val context: Context,
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "image_posts")

    fun observeImagePosts(query: String): Flow<List<ImagePost>> = context.dataStore.data.mapNotNull {
        it[stringPreferencesKey(query)]?.let(Json::decodeFromString)
    }

    suspend fun insertImagePosts(query: String, imagePosts: List<ImagePost>) {
        context.dataStore.edit {
            // It's not the most performant solution, but for the sake of an example project, it's enough. In a real one, I would use a relational database to store it (Room or SQLDelight).
            it[stringPreferencesKey(query)] = Json.encodeToString(imagePosts)
        }
    }
}