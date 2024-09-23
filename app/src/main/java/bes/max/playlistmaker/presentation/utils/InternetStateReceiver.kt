package bes.max.playlistmaker.presentation.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import bes.max.playlistmaker.R
import bes.max.playlistmaker.presentation.utils.ConnectionChecker.isConnected

const val CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"

class InternetStateReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null && p1?.action == CONNECTIVITY_CHANGE_ACTION) {
            if (!isConnected(p0)) {
                Toast.makeText(p0, p0.getString(R.string.no_internet_message), Toast.LENGTH_LONG).show()
            }
        }
    }
}