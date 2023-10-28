package bes.max.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import bes.max.playlistmaker.data.db.entities.PlaylistTrackEntity
import bes.max.playlistmaker.data.db.entities.TrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PlaylistTrackEntity)

    @Update
    suspend fun update(entity: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table")
    suspend fun getAll(): List<PlaylistTrackEntity>

    @Query("SELECT * FROM playlist_track_table WHERE playlist_id=:id")
    suspend fun getAllTracksIdsFromPlaylist(id: Long): List<PlaylistTrackEntity>

    @Query("SELECT" +
            " tracks_table.track_id, track_name, track_artist, track_collection, track_genre, country, track_url, track_time, track_cover, year, adding_date, is_favorite" +
            " FROM" +
            " tracks_table" +
            " LEFT JOIN playlist_track_table ON" +
            " tracks_table.track_id = playlist_track_table.track_id" +
            " LEFT JOIN playlist_table ON" +
            " playlist_track_table.playlist_id = playlist_table.playlist_id" +
            " WHERE playlist_track_table.playlist_id = :id")
    suspend fun getAllTracksFromPlaylist(id: Long): List<TrackEntity>

}