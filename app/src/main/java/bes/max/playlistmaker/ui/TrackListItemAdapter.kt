package bes.max.playlistmaker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListItemAdapter(var onListElementClick: ((track: Track) -> Unit)? = null) :
    RecyclerView.Adapter<TrackListItemAdapter.TrackViewHolder>() {

    var listOfTracks = listOf<Track>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = listOfTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(listOfTracks[position])
        holder.itemView.setOnClickListener {
            if (onListElementClick != null) onListElementClick?.invoke(listOfTracks[position])
        }
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: Track) {
            val trackCover = itemView.findViewById<ImageView>(R.id.track_list_item_track_cover)
            val trackName = itemView.findViewById<TextView>(R.id.track_list_item_track_name)
            val artistName = itemView.findViewById<TextView>(R.id.track_list_item_artist_name)
            val trackTime = itemView.findViewById<TextView>(R.id.track_list_item_track_time)

            Glide.with(itemView)
                .load(model.bigCover)
                .placeholder(R.drawable.ic_picture_not_found)
                .transform(RoundedCorners(10))
                .centerCrop()
                .into(trackCover)

            trackName.setText(model.trackName)
            artistName.setText(model.artistName)
            trackTime.setText(model.trackTime)
        }

    }
}