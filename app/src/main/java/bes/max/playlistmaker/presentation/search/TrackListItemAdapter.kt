package bes.max.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.TrackListItemBinding
import bes.max.playlistmaker.domain.models.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListItemAdapter(
    var onListElementClick: ((track: Track) -> Unit)? = null,
    val onListElementLongClick: ((trackId: Long) -> Unit)? = null
) :
    RecyclerView.Adapter<TrackListItemAdapter.TrackViewHolder>() {

    var listOfTracks = listOf<Track>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackListItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int = listOfTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(listOfTracks[position])
        holder.itemView.setOnClickListener {
            if (onListElementClick != null) onListElementClick?.invoke(listOfTracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onListElementLongClick?.invoke((listOfTracks[position].trackId))
            return@setOnLongClickListener true
        }
    }

    class TrackViewHolder(private val binding: TrackListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Track) {
            Glide.with(itemView)
                .load(model.bigCover)
                .placeholder(R.drawable.ic_picture_not_found)
                .transform(
                    RoundedCorners(
                        itemView.resources.getDimensionPixelSize(
                            R.dimen.search_activity_album_cover_corner_radius
                        )
                    )
                )

                .into(binding.trackListItemTrackCover)

            binding.trackListItemTrackName.setText(model.trackName)
            binding.trackListItemArtistName.setText(model.artistName)
            binding.trackListItemTrackTime.setText(model.trackTime)
        }

    }
}