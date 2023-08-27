package bes.max.playlistmaker.presentation.settings

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.EmailData
import bes.max.playlistmaker.domain.settings.SettingsInteractor
import bes.max.playlistmaker.domain.settings.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _showingToast = MutableLiveData<Int>()
    val showingToast: LiveData<Int> = _showingToast

    val isNightModeActive = settingsInteractor.isNightModeActive().asLiveData(Dispatchers.IO)

    private var switcherChecked = true
    private val handler = Handler(Looper.getMainLooper())
    private val setNightModeRunnable =  Runnable { setIsNightModeActive(switcherChecked) }

    private fun setIsNightModeActive(isNightModeActive: Boolean) {
        viewModelScope.launch {
            settingsInteractor.setIsNightModeActive(isNightModeActive)
        }
    }

    fun setIsNightModeActiveDebounce(isNightModeActive: Boolean) {
        switcherChecked = isNightModeActive
        handler.removeCallbacks(setNightModeRunnable)
        handler.postDelayed(setNightModeRunnable, SET_NIGHT_MODE_DELAY)
    }

    fun shareApp(link: String) {
        try {
            sharingInteractor.shareApp(link)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            _showingToast.value = R.string.settings_implicit_intent_error
            _showingToast.value = 0
        }
    }

    fun contactSupport(
        emailAddress: String,
        emailSubject: String,
        emailText: String
    ) {
        try {
            sharingInteractor.contactSupport(
                EmailData(
                    emailAddress,
                    emailSubject,
                    emailText
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            _showingToast.value = R.string.settings_implicit_intent_error
            _showingToast.value = 0
        }
    }

    fun openUserAgreement(link: String) {
        try {
            sharingInteractor.openUserAgreement(link)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            _showingToast.value = R.string.settings_implicit_intent_error
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
        private const val SET_NIGHT_MODE_DELAY = 200L
    }

}