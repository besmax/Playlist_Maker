package bes.max.playlistmaker.domain.player

import bes.max.playlistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PlayerService {

    val playerState: StateFlow<PlayerState>

    val currentPosition: SharedFlow<Int>

    fun playback()

    fun getCurrentTime(): String

    fun showNotification()

    fun hideNotification()

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Player Service"
        const val SERVICE_NOTIFICATION_ID = 101
        const val EXTRA_ARTIST = "EXTRA_ARTIST"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_URL = "EXTRA_URL"
    }
}