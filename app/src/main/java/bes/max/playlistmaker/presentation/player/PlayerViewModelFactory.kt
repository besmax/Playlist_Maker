package bes.max.playlistmaker.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bes.max.playlistmaker.data.player.PlayerImpl
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.player.PlayerInteractor

class PlayerViewModelFactory(private val track: Track): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(track, PlayerInteractor(PlayerImpl())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}