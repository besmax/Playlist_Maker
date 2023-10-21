package bes.max.playlistmaker.presentation.mediateka.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import bes.max.playlistmaker.databinding.PlaylistListItemBinding
import bes.max.playlistmaker.domain.models.Playlist

class PlaylistItemAdapter(
    private val doOnClick: ((Playlist) -> Unit)? = null
) : ListAdapter<Playlist, PlaylistViewHolder>(PlaylistDiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistListItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        if (doOnClick != null) {
            holder.itemView.setOnClickListener { doOnClick.invoke(item) }
        }
    }
}