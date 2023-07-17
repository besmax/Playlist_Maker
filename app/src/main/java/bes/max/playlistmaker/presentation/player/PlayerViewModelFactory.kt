package bes.max.playlistmaker.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bes.max.playlistmaker.di.Creator
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.player.PlayerInteractor

class PlayerViewModelFactory(private val track: Track): ViewModelProvider.Factory {
    private val creator = Creator(null)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(track, PlayerInteractor(creator.getNewPlayerImpl())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}