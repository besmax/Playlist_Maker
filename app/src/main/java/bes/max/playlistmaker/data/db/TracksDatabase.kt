package bes.max.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 2, entities = [TrackEntity::class], exportSchema = false)
abstract class TracksDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao(): FavoriteTracksDao
}