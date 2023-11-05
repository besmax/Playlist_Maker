package bes.max.playlistmaker.presentation.mediateka.playlists

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.PlaylistListItemGridBinding
import bes.max.playlistmaker.databinding.PlaylistListItemLinearVertBinding
import bes.max.playlistmaker.domain.models.Playlist
import com.bumptech.glide.Glide

class PlaylistViewHolder(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        when (binding) {
            is PlaylistListItemGridBinding -> {
                with(binding) {
                    val coverUri = model.coverPath?.toUri()
                    Glide.with(binding.root)
                        .load(coverUri)
                        .centerCrop()
                        .placeholder(R.drawable.playlist_placeholder_grid)
                        .into(playlistCover)
                    playlistName.text = model.name
                    tracksQty.text = itemView.context.resources.getQuantityString(
                        R.plurals.tracks_number,
                        model.tracksNumber,
                        model.tracksNumber
                    )
                }
            }

            is PlaylistListItemLinearVertBinding -> {
                with(binding) {
                    val coverUri = model.coverPath?.toUri()
                    Glide.with(binding.root)
                        .load(coverUri)
                        .centerCrop()
                        .placeholder(R.drawable.playlist_placeholder_grid)
                        .into(playlistCover)
                    playlistName.text = model.name
                    tracksQty.text = itemView.context.resources.getQuantityString(
                        R.plurals.tracks_number,
                        model.tracksNumber,
                        model.tracksNumber
                    )
                }
            }
        }
    }

}