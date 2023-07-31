package bes.max.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.settings.SettingsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    val isNightModeActive = settingsInteractor.isNightModeActive().asLiveData(Dispatchers.IO)

    fun setIsNightModeActive(isNightModeActive: Boolean) {
        viewModelScope.launch {
            settingsInteractor.setIsNightModeActive(isNightModeActive)
        }
    }

}