package dev.olek.payback.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.olek.domain.models.ImagePost
import dev.olek.payback.R
import dev.olek.payback.viewmodels.ImagePostsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImagePostsScreen(
    viewModel: ImagePostsViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    var postConfirmationDialogVisibility: Pair<Boolean, Long> by rememberSaveable {
        mutableStateOf(false to -1) // isVisible, imageId
    }

    ImagePostsScreenContent(
        state = state,
        onQueryChange = viewModel::onSearchQueryUpdated,
        onImagePostClicked = {
            postConfirmationDialogVisibility = true to it.id
        }
    )

    if (postConfirmationDialogVisibility.first) {
        PostConfirmationDialog(
            imagePostId = postConfirmationDialogVisibility.second,
            onConfirmation = {},
            onDismissRequest = {
                postConfirmationDialogVisibility = false to -1
            }
        )
    }
    
    LaunchedEffect(state.error, context) {
        state.error?.let { 
            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun ImagePostsScreenContent(
    state: ImagePostsViewModel.UiState,
    onQueryChange: (String) -> Unit,
    onImagePostClicked: (ImagePost) -> Unit,
) {
    Column {
        ImageSearchBar(query = state.searchQuery, onQueryChange = onQueryChange, isLoading = state.isLoading)
        ImageList(imagePosts = state.imagePosts, onImagePostClicked = onImagePostClicked)
    }
}

@Composable
fun ImageSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isLoading: Boolean,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        label = { Text(stringResource(R.string.search)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(24.dp))
            }
        }
    )
}

@Composable
fun ImageList(
    imagePosts: List<ImagePost>,
    onImagePostClicked: (ImagePost) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(170.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(imagePosts) { image ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .animateItem()
                        .clickable { onImagePostClicked(image) },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image.previewUrl)
                        .placeholder(R.drawable.image_outline)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.image_with_tags, image.tags),
                )
            }
        },
    )
}

@Composable
private fun PostConfirmationDialog(
    imagePostId: Long,
    onDismissRequest: () -> Unit,
    onConfirmation: (Long) -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.do_you_want_to_see_full_image))
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation(imagePostId) }
            ) {
                Text(stringResource(R.string.yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(stringResource(R.string.no))
            }
        }
    )
}