package bes.max.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bes.max.playlistmaker.data.db.entities.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_table WHERE is_favorite ORDER BY adding_date")
    suspend fun getAllFavoriteTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM tracks_table")
    suspend fun getAllIdsOfFavoriteTracks(): List<Long>

    @Query("SELECT * FROM tracks_table WHERE track_id=:id")
    suspend fun getTrackById(id: Long): TrackEntity

}