package com.mato.studio.core.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import com.mato.studio.core.database.entity.AppState
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao{
    @Query("Select * from app_state where id = 1")
    fun observeAppState(): Flow<AppState?>

    @Query("Update app_state set hasSeenOnboarding = :hasSeenOnboarding where id = 1")
    suspend fun updateHasSeenOnboarding(hasSeenOnboarding: Boolean)
}