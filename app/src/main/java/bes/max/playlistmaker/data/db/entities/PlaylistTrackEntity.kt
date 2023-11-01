package bes.max.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "playlist_track_table",
    primaryKeys = ["playlist_id", "track_id"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            childColumns = arrayOf("playlist_id"),
            parentColumns = arrayOf("playlist_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TrackEntity::class,
            childColumns = arrayOf("track_id"),
            parentColumns = arrayOf("track_id"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PlaylistTrackEntity(

    @ColumnInfo("playlist_id")
    val playlistId: Long,

    @ColumnInfo("track_id")
    val trackId: Long,

    )
