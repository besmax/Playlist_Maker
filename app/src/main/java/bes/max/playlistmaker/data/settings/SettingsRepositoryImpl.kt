package bes.max.playlistmaker.data.settings

import bes.max.playlistmaker.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao
) : SettingsRepository {

    override fun isNightModeActive(): Flow<Boolean> =
        settingsDao.isNightModeActive()

    override suspend fun setIsNightModeActive(isNightModeActive: Boolean) {
        settingsDao.setIsNightModeActive(isNightModeActive)
    }


}