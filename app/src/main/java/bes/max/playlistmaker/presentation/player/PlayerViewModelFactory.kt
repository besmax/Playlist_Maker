package bes.max.playlistmaker.presentation.player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bes.max.playlistmaker.di.Creator
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.player.PlayerInteractorImpl

class PlayerViewModelFactory(private val track: Track, context: Context): ViewModelProvider.Factory {

    private val creator = Creator(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(track, PlayerInteractorImpl(creator.getNewPlayerImpl())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}