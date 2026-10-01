package com.mato.studio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mato.studio.navigation.NavGraph
import com.mato.studio.presentation.onboarding.OnboardingViewModel
import com.mato.studio.ui.theme.StudioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val onboardingViewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            !onboardingViewModel.isReady.value
        }

        setContent {
            StudioTheme {
                val hasSeenOnboarding by onboardingViewModel.hasSeenOnboarding.collectAsState()

                hasSeenOnboarding?.let { seen ->
                    NavGraph(hasSeenOnboarding = seen)
                }
            }
        }
    }
}
