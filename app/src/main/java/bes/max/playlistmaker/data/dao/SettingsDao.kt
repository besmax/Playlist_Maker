package bes.max.playlistmaker.data.dao

import kotlinx.coroutines.flow.Flow

interface SettingsDao {

    fun isNightModeActive() : Flow<Boolean>

    suspend fun setIsNightModeActive(isNightModeActive: Boolean)

}