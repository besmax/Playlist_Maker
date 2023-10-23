package bes.max.playlistmaker.presentation.mediateka.playlist

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.PlaylistListItemGridBinding
import bes.max.playlistmaker.databinding.PlaylistListItemLinearVertBinding
import bes.max.playlistmaker.domain.models.Playlist

class PlaylistViewHolder(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        when (binding) {
            is PlaylistListItemGridBinding -> {
                with(binding) {
                    val coverUri = model.coverPath?.toUri()
                    if (model.coverPath != null) playlistCover.setImageURI(coverUri)
                    else playlistCover.setImageResource(R.drawable.ic_picture_not_found)
                    playlistName.text = model.name
                    val trackQty =
                        if (model.tracksNumber.toString() != "null") model.tracksNumber.toString() else "0"
                    tracksQty.text = itemView.context.getString(R.string.number_of_tracks, trackQty)
                }
            }

            is PlaylistListItemLinearVertBinding -> {
                with(binding) {
                    val coverUri = model.coverPath?.toUri()
                    if (coverUri != null) playlistCover.setImageURI(coverUri)
                    else playlistCover.setImageResource(R.drawable.ic_picture_not_found)
                    playlistName.text = model.name
                    val trackQty =
                        if (model.tracksNumber.toString() != "null") model.tracksNumber.toString() else "0"
                    tracksQty.text = itemView.context.getString(R.string.number_of_tracks, trackQty)
                }
            }
        }
    }

}