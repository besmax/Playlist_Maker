package bes.max.playlistmaker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bes.max.playlistmaker.domain.models.Track

class PlayerViewModelFactory(private val track: Track): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(track) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}