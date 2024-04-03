package bes.max.playlistmaker.presentation.player

interface PlayerService {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Player Service"
        const val NOTIFICATION_CHANNEL_NAME = "Media player notification"
        const val INTENT_FILTER_PAUSE_ACTION = "INTENT_FILTER_PAUSE_ACTION"
        const val ACTION_START_FOREGROUND = "ACTION_START_FOREGROUND"
        const val ACTION_STOP_FOREGROUND = "ACTION_STOP_FOREGROUND"
        const val EXTRA_ARTIST = "EXTRA_ARTIST"
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}