package dev.olek.payback.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.olek.domain.ImagePostRepository
import dev.olek.domain.models.ImagePost
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
class ImagePostViewModel(
    @InjectedParam private val postId: Long,
    private val repository: ImagePostRepository,
) : ViewModel() {

    private val mutableState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(onErrorShown = ::onErrorShown)
    )
    val state: StateFlow<UiState> = mutableState.asStateFlow()


    init {
        loadImagePost()
    }

    private fun loadImagePost() {
        viewModelScope.launch {
            mutableState.update { it.copy(isLoading = true) }
            runCatching {
                repository.getImagePost(postId)
            }.onFailure { error ->
                Logger.e(throwable = error) { "Failed to get image post" }
                mutableState.update { it.copy(isLoading = false, error = error.message) }
            }.onSuccess { imagePost ->
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        imagePost = imagePost
                    )
                }
            }
        }
    }

    private fun onErrorShown() {
        mutableState.update { it.copy(error = null) }
    }

    @Immutable
    data class UiState(
        val isLoading: Boolean = true,
        val error: String? = null,
        val onErrorShown: () -> Unit = {},
        val imagePost: ImagePost? = null,
    )
}