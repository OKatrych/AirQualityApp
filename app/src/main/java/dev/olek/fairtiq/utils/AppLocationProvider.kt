package dev.olek.fairtiq.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dev.olek.domain.models.Location
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class AppLocationProvider(
    context: Context,
) {

    private val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Location {
        val fusedLocation = runCatching {
            fusedLocationProvider.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }
            ).await()
        }.getOrNull() ?: fusedLocationProvider.lastLocation.await()
        return Location(latitude = fusedLocation.latitude, longitude = fusedLocation.longitude)
    }
}