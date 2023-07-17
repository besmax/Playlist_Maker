package bes.max.playlistmaker.data.dao

import android.content.Context
import bes.max.playlistmaker.domain.models.Track
import com.google.gson.Gson

private const val SHARED_PREF_KEY = "search_history_preferences_key"

class SearchHistoryDaoImpl(context: Context) : SearchHistoryDao {

    private var history: MutableList<Track> = arrayListOf()
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0)

    override fun saveTrack(track: Track) {
        if (sharedPreferences.contains(SHARED_PREF_KEY)) {
            val jsonString = sharedPreferences.getString(SHARED_PREF_KEY, "")
            if (!jsonString.isNullOrBlank()) {
                history = Gson().fromJson(jsonString, Array<Track>::class.java).toMutableList()
                if (history.contains(track)) {
                    history.remove(track)
                }
                if (history.size > 9) {
                    history.removeFirst()
                }
            }
        }
        history.add(track)
        sharedPreferences.edit()
            .putString(SHARED_PREF_KEY, Gson().toJson(history))
            .apply()
    }

    override fun getHistoryTracks(): List<Track> {
        history.clear()
        if (sharedPreferences.contains(SHARED_PREF_KEY)) {
            val jsonString = sharedPreferences.getString(SHARED_PREF_KEY, "")
            if (!jsonString.isNullOrBlank()) {
                history = Gson().fromJson(jsonString, Array<Track>::class.java).toMutableList()
            }
        }
        return history.toList()
    }

    override fun clearTracksHistory() {
        history.clear()
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}