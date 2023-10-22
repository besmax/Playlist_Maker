package bes.max.playlistmaker.data.mappers

import bes.max.playlistmaker.data.db.entities.PlaylistEntity
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlaylistDbMapper {

    private val typeToken = object : TypeToken<List<Track>>() {}.type
    fun map(playlistEntity: PlaylistEntity): Playlist =
        Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            coverPath = playlistEntity.coverPath,
            tracks = Gson().fromJson(playlistEntity.tracks, typeToken),
            tracksNumber = playlistEntity.tracksNumber
        )

    fun map(playlist: Playlist): PlaylistEntity =
        PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverPath,
            tracks = Gson().toJson(playlist.tracks),
            tracksNumber = playlist.tracksNumber
        )

}