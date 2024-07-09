package dev.olek.payback.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.olek.domain.models.ImagePost
import dev.olek.payback.R
import dev.olek.payback.viewmodels.ImagePostViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ImagePostScreen(
    postId: Long,
    viewModel: ImagePostViewModel = koinViewModel(parameters = { parametersOf(postId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Surface(Modifier.fillMaxSize()) {
        if (state.isLoading) {
            Loading()
        } else {
            ImagePostScreenContent(state)
        }
    }

    LaunchedEffect(state.error, context) {
        state.error?.let {
            state.onErrorShown()
            Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun ImagePostScreenContent(
    state: ImagePostViewModel.UiState,
) {
    Column(
        Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        state.imagePost?.let { post ->
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.largeImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Inside,
                contentDescription = stringResource(R.string.image_with_tags, post.tags),
            )
            StatisticsSection(post)
            TagsSection(post.tags)
        }
    }
}

@Composable
private fun TagsSection(
    tags: List<String>,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(tags) {
            Text("#$it")
        }
    }
}

@Composable
private fun StatisticsSection(post: ImagePost) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconWithText(Icons.Default.Favorite, post.likesCount.toString())
        IconWithText(Icons.Default.MailOutline, post.commentsCount.toString())
        IconWithText(Icons.Default.Person, post.userName)
    }
}

@Composable
private fun IconWithText(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null)
        Text(text)
    }
}

@Composable
private fun Loading() = Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    CircularProgressIndicator()
}