package bes.max.playlistmaker.data.mappers

import bes.max.playlistmaker.data.db.entities.PlaylistEntity
import bes.max.playlistmaker.domain.models.Playlist

object PlaylistDbMapper {

    fun map(playlistEntity: PlaylistEntity): Playlist =
        Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            coverPath = playlistEntity.coverPath,
            tracks = null,
            tracksNumber = playlistEntity.tracksNumber
        )

    fun map(playlist: Playlist): PlaylistEntity =
        PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverPath,
            tracksNumber = playlist.tracksNumber
        )

}
