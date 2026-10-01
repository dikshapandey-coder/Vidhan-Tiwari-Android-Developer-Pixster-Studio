package com.mato.studio.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mato.studio.repo.AppStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appStateRepository: AppStateRepository
) : ViewModel(){

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _hasSeenOnboarding = MutableStateFlow<Boolean?>(null)
    val hasSeenOnboarding: StateFlow<Boolean?> = _hasSeenOnboarding.asStateFlow()

    init {
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val seen = appStateRepository.hasSeenOnboarding.first()

            val elapsed = System.currentTimeMillis() - startTime
            val remainingDelay = (2500L - elapsed).coerceAtLeast(0L)
            delay(remainingDelay)

            _hasSeenOnboarding.value = seen
            _isReady.value = true
        }
    }

    fun updateHasSeenOnboarding(hasSeenOnboarding: Boolean) {
        viewModelScope.launch {
            appStateRepository.updateHasSeenOnboarding(hasSeenOnboarding)
            _hasSeenOnboarding.value = hasSeenOnboarding
        }
    }

}