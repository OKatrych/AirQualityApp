package dev.olek.payback.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.olek.domain.ImagePostRepository
import dev.olek.domain.models.ImagePost
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ImagePostsViewModel(
    private val repository: ImagePostRepository,
) : ViewModel() {

    companion object {
        private const val DEFAULT_SEARCH_QUERY = "fruits"
        private const val DEBOUNCE_TIMEOUT = 500L
    }

    private val mutableState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(onErrorShown = ::onErrorShown)
    )
    val state: StateFlow<UiState> = mutableState.asStateFlow()

    private var searchJob: Job? = null
    private var firstEmission = true
    
    init {
        onSearchQueryUpdated(DEFAULT_SEARCH_QUERY)
    }

    fun onSearchQueryUpdated(searchQuery: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            runCatching {
                debounceSearch()
                repository.observeImagePosts(searchQuery)
                    .catch { error ->
                        Logger.e(throwable = error) { "Failed to get image posts" }
                        mutableState.update { it.copy(isLoading = false, error = error.message) }
                    }
                    .onEach { imagePosts ->
                        println("STATE: got result ${imagePosts.size}")
                        mutableState.update { it.copy(isLoading = false, imagePosts = imagePosts) }
                    }
                    .collect()
            }.onFailure {
                Logger.e(throwable = it) { "Pizda" }
            }
        }
        mutableState.update { it.copy(searchQuery = searchQuery) }
    }

    private fun onErrorShown() {
        mutableState.update { it.copy(error = null) }
    }
    
    private suspend fun debounceSearch() {
        if (!firstEmission) {
            delay(DEBOUNCE_TIMEOUT)
        } else {
            firstEmission = true
        }
    }

    @Immutable
    data class UiState(
        val isLoading: Boolean = true,
        val error: String? = null,
        val onErrorShown: () -> Unit,
        val searchQuery: String = DEFAULT_SEARCH_QUERY,
        val imagePosts: List<ImagePost> = emptyList(),
    )
}