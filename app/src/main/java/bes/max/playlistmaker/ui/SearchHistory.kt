package bes.max.playlistmaker.ui

import android.content.SharedPreferences
import bes.max.playlistmaker.model.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences, private val key: String) {

    var history: MutableList<Track> = arrayListOf()

    fun saveTrack(track: Track) {
        if (sharedPreferences.contains(key)) {
            val jsonString = sharedPreferences.getString(key, "")
            if (!jsonString.isNullOrBlank()) {
                history = Gson().fromJson(jsonString, Array<Track>::class.java).toMutableList()
                if (history.contains(track)) {
                    history.remove(track)
                }
                if (history.size > 9) {
                    history.removeLast()
                }
            }
        }
        history.add(track)
        sharedPreferences.edit()
            .putString(key, Gson().toJson(history))
            .apply()
    }

    fun getHistoryTracks(){
        history.clear()
        if (sharedPreferences.contains(key)) {
            val jsonString = sharedPreferences.getString(key, "")
            if (!jsonString.isNullOrBlank()) {
                history = Gson().fromJson(jsonString, Array<Track>::class.java).toMutableList()
            }
        }
    }

    fun clearTracksHistory() {
        history.clear()
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}