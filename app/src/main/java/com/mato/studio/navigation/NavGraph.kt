package com.mato.studio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mato.studio.presentation.home.HomeScreen
import com.mato.studio.presentation.onboarding.OnBoardingScreen
import com.mato.studio.presentation.onboarding.OnboardingViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    onBoardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val hasSeenOnboarding by onBoardingViewModel.hasSeenOnboarding.collectAsState(initial = false)
    val startDestination = if (hasSeenOnboarding) Route.Home.route else Route.Onboarding.route

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Route.Onboarding.route) {
            OnBoardingScreen(
                onStarted = {
                    onBoardingViewModel.updateHasSeenOnboarding(true)
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Route.Home.route) {
            HomeScreen()
        }
    }
}