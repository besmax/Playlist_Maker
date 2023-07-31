package bes.max.playlistmaker.domain.settings

import bes.max.playlistmaker.domain.api.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
    ) : SettingsInteractor {

    override fun isNightModeActive(): Flow<Boolean> =
        settingsRepository.isNightModeActive()

    override suspend fun setIsNightModeActive(isNightModeActive: Boolean) {
        settingsRepository.setIsNightModeActive(isNightModeActive)
    }
}