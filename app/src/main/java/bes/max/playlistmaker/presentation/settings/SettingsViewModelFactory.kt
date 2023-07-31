package bes.max.playlistmaker.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bes.max.playlistmaker.di.Creator

class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val creator = Creator(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(creator.getSettingsInteractor(context), creator.getSharingInteractor()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}