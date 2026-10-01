package com.mato.studio.navigation

sealed interface Route{
    data object Onboarding: Route
    data object CalculatorScreen: Route
}