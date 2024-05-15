package dev.olek.fairtiq.viewmodels

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import dev.olek.domain.AirQualityRepository
import dev.olek.domain.models.AirQualityReport
import dev.olek.fairtiq.R
import dev.olek.fairtiq.utils.AppLocationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AirQualityViewModel(
    private val repository: AirQualityRepository,
    private val locationProvider: AppLocationProvider,
): ViewModel() {

    private val mutableState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                repository.getLatestAirQualityReportFor(
                    useCache = false,
                    location = locationProvider.getLastLocation(),
                )
            }.onSuccess { report ->
                mutableState.update { report.toUiState() }
            }.onFailure {
                Logger.e(throwable = it) { "Failed to get air quality report" }
                mutableState.update {
                    UiState(
                        isLoading = false,
                        textRes = R.string.error_message,
                    )
                }
            }
        }
    }

    private fun AirQualityReport.toUiState(): UiState {
        return UiState(
            isLoading = false,
            backgroundColorRes = when (this.qualityIndex) {
                1 -> R.color.quality_good
                2 -> R.color.quality_fair
                3 -> R.color.quality_moderate
                4 -> R.color.quality_poor
                5 -> R.color.quality_very_poor
                else -> null
            },
            textRes = when (this.qualityIndex) {
                1 -> R.string.quality_good
                2 -> R.string.quality_fair
                3 -> R.string.quality_moderate
                4 -> R.string.quality_poor
                5 -> R.string.quality_very_poor
                else -> null
            },
        )
    }

    @Immutable
    data class UiState(
        val isLoading: Boolean = true,
        @ColorRes val backgroundColorRes: Int? = null,
        @StringRes val textRes: Int? = null,
    )
}