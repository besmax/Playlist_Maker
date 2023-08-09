package bes.max.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.models.EmailData
import bes.max.playlistmaker.domain.settings.SettingsInteractor
import bes.max.playlistmaker.domain.settings.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    val isNightModeActive = settingsInteractor.isNightModeActive().asLiveData(Dispatchers.IO)

    fun setIsNightModeActive(isNightModeActive: Boolean) {
        viewModelScope.launch {
            settingsInteractor.setIsNightModeActive(isNightModeActive)
        }
    }

    fun shareApp(link: String) {
        sharingInteractor.shareApp(link)
    }

    fun contactSupport(
        emailAddress: String,
        emailSubject: String,
        emailText: String
    ) {
        sharingInteractor.contactSupport(
            EmailData(
                emailAddress,
                emailSubject,
                emailText
            )
        )

    }

    fun openUserAgreement(link: String) {
        sharingInteractor.openUserAgreement(link)
    }

}