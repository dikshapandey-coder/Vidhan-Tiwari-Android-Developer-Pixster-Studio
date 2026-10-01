package com.mato.studio.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.mato.studio.core.database.entity.AppState
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao {
    @Query("SELECT * FROM app_state WHERE id = 1")
    fun observeAppState(): Flow<AppState?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppState(appState: AppState)

    suspend fun updateHasSeenOnboarding(hasSeenOnboarding: Boolean) {
        saveAppState(AppState(id = 1, hasSeenOnboarding = hasSeenOnboarding))
    }
}