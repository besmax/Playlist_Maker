package bes.max.playlistmaker.presentation.mediateka.playlists

import androidx.recyclerview.widget.DiffUtil
import bes.max.playlistmaker.domain.models.Playlist

class PlaylistDiffItemCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean =
        oldItem == newItem
}