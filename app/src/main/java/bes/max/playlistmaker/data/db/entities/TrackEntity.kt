package bes.max.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
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

    @ColumnInfo("track_country")
    val country: String,

    @ColumnInfo("track_url")
    val previewUrl: String,

    @ColumnInfo("track_time")
    val trackTime: String,

    @ColumnInfo("track_cover")
    val bigCover: String,

    @ColumnInfo("track_year")
    val year: String,

    @ColumnInfo("adding_date")
    val addingDate: Long

)
