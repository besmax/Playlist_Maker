package bes.max.playlistmaker.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("playlist_id")
    val id: Long = 0,
    val name: String,
    val description: String?,

    @ColumnInfo(name = "cover_path")
    val coverPath: String?,

    @ColumnInfo(name = "tracks_number")
    val tracksNumber: Int = 0
)
