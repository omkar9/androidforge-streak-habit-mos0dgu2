package com.androidforge.streakhappit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.androidforge.streakhappit.navigation.AppNavGraph
import com.androidforge.streakhappit.presentation.theme.StreakHabitTheme
import dagger.hilt.android.AndroidEntryPoint
import com.androidforge.streakhappit.domain.repository.SettingsRepository
import javax.inject.Inject
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // NEW: Import for Splash Screen API

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // NEW: Call before super.onCreate() and setContent()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val userPrefersDarkTheme by settingsRepository.getSettingsFlow().collectAsState(initial = isSystemInDarkTheme())

            StreakHabitTheme(darkTheme = userPrefersDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StreakHabitTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            AppNavGraph(navController = navController)
        }
    }
}