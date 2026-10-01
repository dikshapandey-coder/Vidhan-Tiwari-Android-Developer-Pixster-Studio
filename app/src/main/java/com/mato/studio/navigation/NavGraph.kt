package com.mato.studio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mato.studio.presentation.onboarding.OnBoardingScreen
import com.mato.studio.presentation.onboarding.OnboardingViewModel

@Composable
fun NavGraph( modifier: Modifier = Modifier,
             onBoardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val hasSeenOnboarding = onBoardingViewModel.hasSeenOnboarding.collectAsState(initial = false)
    val startDestination = if (hasSeenOnboarding.value) Route.CalculatorScreen.toString() else Route.Onboarding.toString()

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        composable(Route.Onboarding.toString()) {
            OnBoardingScreen (
                onStarted = {
                    onBoardingViewModel.updateHasSeenOnboarding(true)

                    navController.navigate(Route.CalculatorScreen.toString()) {
                        popUpTo(Route.Onboarding.toString()) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Route.CalculatorScreen.toString()) {
            //CalculatorScreen()
        }
    }

}