package bes.max.playlistmaker.data.settings

import kotlinx.coroutines.flow.Flow

interface SettingsDao {

    fun isNightModeActive() : Flow<Boolean>

    suspend fun setIsNightModeActive(isNightModeActive: Boolean)

}