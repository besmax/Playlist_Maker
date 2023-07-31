package bes.max.playlistmaker.domain.api

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun isNightModeActive(): Flow<Boolean>

    suspend fun setIsNightModeActive(isNightModeActive: Boolean)

}