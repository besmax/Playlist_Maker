package bes.max.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bes.max.playlistmaker.data.db.dao.FavoriteTracksDao
import bes.max.playlistmaker.data.db.dao.PlaylistsDao
import bes.max.playlistmaker.data.db.entities.PlaylistEntity
import bes.max.playlistmaker.data.db.entities.TrackEntity

@Database(version = 3, entities = [TrackEntity::class, PlaylistEntity::class], exportSchema = false)
abstract class TracksDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao(): FavoriteTracksDao

    abstract fun playlistDao(): PlaylistsDao
}