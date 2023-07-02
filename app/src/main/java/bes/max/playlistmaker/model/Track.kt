package bes.max.playlistmaker.model

import android.icu.text.SimpleDateFormat
import java.util.*

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String = "",
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String

) {
    val trackTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    val bigCover: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    val year: String
        get() = releaseDate.take(4)

}
