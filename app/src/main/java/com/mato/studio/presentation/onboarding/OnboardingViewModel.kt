package com.mato.studio.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mato.studio.repo.AppStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appStateRepository: AppStateRepository
) : ViewModel(){
    val hasSeenOnboarding = appStateRepository.hasSeenOnboarding

    fun updateHasSeenOnboarding(hasSeenOnboarding: Boolean) {
        viewModelScope.launch {
            appStateRepository.updateHasSeenOnboarding(hasSeenOnboarding)
        }
    }

}