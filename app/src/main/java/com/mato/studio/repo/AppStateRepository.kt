package com.mato.studio.repo

import com.mato.studio.core.database.dao.AppStateDao
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppStateRepository @Inject constructor(
    private val appStateDao: AppStateDao
){
    val hasSeenOnboarding = appStateDao.observeAppState().map { it?.hasSeenOnboarding ?: false }

    suspend fun updateHasSeenOnboarding(hasSeenOnboarding: Boolean) = appStateDao.updateHasSeenOnboarding(hasSeenOnboarding)

}