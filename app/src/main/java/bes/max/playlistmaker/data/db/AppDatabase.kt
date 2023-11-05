package bes.max.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bes.max.playlistmaker.data.db.dao.PlaylistTrackDao
import bes.max.playlistmaker.data.db.dao.PlaylistsDao
import bes.max.playlistmaker.data.db.dao.TrackDao
import bes.max.playlistmaker.data.db.entities.PlaylistEntity
import bes.max.playlistmaker.data.db.entities.PlaylistTrackEntity
import bes.max.playlistmaker.data.db.entities.TrackEntity

@Database(
    version = 5,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistsDao(): PlaylistsDao

    abstract fun trackDao(): TrackDao

    abstract fun playlistTrackDao(): PlaylistTrackDao
}