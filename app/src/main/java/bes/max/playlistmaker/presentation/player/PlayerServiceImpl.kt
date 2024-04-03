package bes.max.playlistmaker.presentation.player

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class PlayerServiceImpl : Service(), PlayerService {

    private val binder = PlayerServiceImplBinder()

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    inner class PlayerServiceImplBinder : Binder() {
        fun getService(): PlayerServiceImpl = this@PlayerServiceImpl
    }
}