package com.mato.studio.navigation

sealed class Route(val route: String) {
    data object Onboarding : Route("onboarding")
    data object Home : Route("home")
}