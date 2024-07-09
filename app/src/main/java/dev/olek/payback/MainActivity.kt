package dev.olek.payback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.olek.payback.ui.screens.ImagePostScreen
import dev.olek.payback.ui.screens.ImagePostsScreen
import dev.olek.payback.ui.theme.PaybackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaybackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        val navController = rememberNavController()
                        NavHost(navController, "imagePosts") {
                            composable("imagePosts") {
                                ImagePostsScreen(onImagePostClickConfirmed = {
                                    navController.navigate("imagePosts/$it")
                                })
                            }
                            composable("imagePosts/{postId}") {
                                ImagePostScreen(it.arguments!!.getString("postId")!!.toLong())
                            }
                        }

                    }
                }
            }
        }
    }
}