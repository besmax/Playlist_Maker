package bes.max.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class TracksDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao() : FavoriteTracksDao
}