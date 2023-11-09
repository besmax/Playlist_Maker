package bes.max.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey @ColumnInfo("track_id")
    val trackId: Long,

    @ColumnInfo("track_name")
    val trackName: String,

    @ColumnInfo("track_artist")
    val artistName: String,

    @ColumnInfo("track_collection")
    val collectionName: String,

    @ColumnInfo("track_genre")
    val primaryGenreName: String,

    val country: String,

    @ColumnInfo("track_url")
    val previewUrl: String,

    @ColumnInfo("track_time")
    val trackTime: String,

    @ColumnInfo("track_time_millis")
    val trackTimeMillis: Long,

    @ColumnInfo("track_cover")
    val bigCover: String,

    val year: String,

    @ColumnInfo("adding_date")
    val addingDate: Long,

    @ColumnInfo("is_favorite")
    val isFavorite: Boolean
)
