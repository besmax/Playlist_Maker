package bes.max.playlistmaker.data.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchHistoryDaoImpl(context: Context) : SearchHistoryDao {

    private var history: MutableList<Track> = arrayListOf()
    private val key = context.getString(R.string.search_history_preferences_key)
    private val sharedPreferences = context.getSharedPreferences(
        key,
        AppCompatActivity.MODE_PRIVATE
    )

    override fun saveTrack(track: Track) {
        if (sharedPreferences.contains(key)) {
            val jsonString = sharedPreferences.getString(key, "")
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
            .putString(key, Gson().toJson(history))
            .apply()
    }

    override fun getHistoryTracks(): List<Track> {
        history.clear()
        if (sharedPreferences.contains(key)) {
            val jsonString = sharedPreferences.getString(key, "")
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