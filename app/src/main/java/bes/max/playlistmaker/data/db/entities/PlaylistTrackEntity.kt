package bes.max.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "playlist_track_table",
    primaryKeys = ["track_id", "playlist_id"],
)
data class PlaylistTrackEntity(

    @ColumnInfo("track_id")
    val trackId: Long,

    @ColumnInfo("playlist_id")
    val playlistId: Long,

    )
