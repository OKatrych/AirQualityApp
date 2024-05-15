package dev.olek.fairtiq

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import co.touchlab.kermit.Logger
import dev.olek.fairtiq.ui.screens.AirQualityScreen
import dev.olek.fairtiq.ui.theme.FAIRTIQTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FAIRTIQTheme {
                var showScreen by rememberSaveable { mutableStateOf(false) }
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        showScreen = true
                    } else {
                        Logger.d("Permission Denied")
                    }
                }

                LaunchedEffect(Unit) {
                    if (isLocationPermissionGranted()) {
                        showScreen = true
                    } else {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }

                if (showScreen) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AirQualityScreen()
                    }
                }
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}