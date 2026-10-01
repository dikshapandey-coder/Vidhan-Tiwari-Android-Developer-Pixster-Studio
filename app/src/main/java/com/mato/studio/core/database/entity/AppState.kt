package com.mato.studio.core.database.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "app_state")
data class AppState(
    @PrimaryKey
    val id: Int = 1,
    val hasSeenOnboarding: Boolean = false
)