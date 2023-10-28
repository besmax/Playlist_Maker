package bes.max.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "playlist_track_table",
    primaryKeys = ["track_id", "playlist_id"],
    foreignKeys = [
        ForeignKey(
            entity = TrackEntity::class,
            childColumns = arrayOf("track_id"),
            parentColumns = arrayOf("track_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlaylistEntity::class,
            childColumns = arrayOf("playlist_id"),
            parentColumns = arrayOf("playlist_id"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PlaylistTrackEntity(

    @ColumnInfo("track_id")
    val trackId: Long,

    @ColumnInfo("playlist_id")
    val playlistId: Long,

    )
