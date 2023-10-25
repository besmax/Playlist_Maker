package bes.max.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String?,
    @ColumnInfo(name = "cover_path")
    val coverPath: String?,
    val tracks: String?,
    @ColumnInfo(name = "tracks_number")
    val tracksNumber: Int = 0
)
