package bes.max.playlistmaker.presentation.mediateka.playlist

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.PlaylistListItemBinding
import bes.max.playlistmaker.domain.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Playlist) {
            with(binding) {
                val coverUri = model.coverPath?.toUri()
                if (coverUri != null) playlistCover.setImageURI(coverUri)
                else playlistCover.setImageResource(R.drawable.ic_picture_not_found)
                playlistName.text = model.name
                val trackQty = model.tracksNumber.toString()
                tracksQty.text = if(trackQty != "null") trackQty else "0"
            }
        }

}