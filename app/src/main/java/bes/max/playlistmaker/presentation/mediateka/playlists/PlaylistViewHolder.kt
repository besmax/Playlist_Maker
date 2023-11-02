package bes.max.playlistmaker.presentation.mediateka.playlists

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.PlaylistListItemGridBinding
import bes.max.playlistmaker.databinding.PlaylistListItemLinearVertBinding
import bes.max.playlistmaker.domain.models.Playlist
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
                        .placeholder(R.drawable.ic_picture_not_found)
                        .transform(
                            RoundedCorners(
                                itemView.resources.getDimensionPixelSize(
                                    R.dimen.activity_player_album_cover_corner_radius
                                )
                            )
                        )
                        .into(playlistCover)
                    playlistName.text = model.name
                    val trackQty =
                        if (model.tracksNumber.toString() != "null") model.tracksNumber.toString() else "0"
                    tracksQty.text = itemView.context.getString(R.string.number_of_tracks, trackQty)
                }
            }

            is PlaylistListItemLinearVertBinding -> {
                with(binding) {
                    val coverUri = model.coverPath?.toUri()
                    Glide.with(binding.root)
                        .load(coverUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_picture_not_found)
                        .transform(
                            RoundedCorners(
                                itemView.resources.getDimensionPixelSize(
                                    R.dimen.search_activity_album_cover_corner_radius
                                )
                            )
                        )
                        .into(playlistCover)
                    playlistName.text = model.name
                    val trackQty =
                        if (model.tracksNumber.toString() != "null") model.tracksNumber.toString() else "0"
                    tracksQty.text = itemView.context.getString(R.string.number_of_tracks, trackQty)
                }
            }
        }
    }

}