package dev.olek.fairtiq.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.olek.fairtiq.R
import dev.olek.fairtiq.viewmodels.AirQualityViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AirQualityScreen(
    viewModel: AirQualityViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(state.backgroundColorRes ?: R.color.white)),
        contentAlignment = Alignment.Center,
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = stringResource(state.textRes ?: R.string.unknown_air_quality))
        }
    }
}