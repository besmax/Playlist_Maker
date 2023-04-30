package bes.max.playlistmaker

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bes.max.playlistmaker.model.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListItemAdapter(private val listOfTracks: ArrayList<Track>) : RecyclerView.Adapter<TrackListItemAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = listOfTracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(listOfTracks[position])
    }

    class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(model: Track) {
            val trackCover = itemView.findViewById<ImageView>(R.id.track_list_item_track_cover)
            val trackName = itemView.findViewById<TextView>(R.id.track_list_item_track_name)
            val artistName = itemView.findViewById<TextView>(R.id.track_list_item_artist_name)
            val trackTime = itemView.findViewById<TextView>(R.id.track_list_item_track_time)

            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.error)
                .transform(RoundedCorners(10))
                .centerCrop()
                .into(trackCover)

            trackName.setText(model.trackName)
            trackName.maxLines = 1

            artistName.setText(model.artistName)
            artistName.maxLines = 1

            trackTime.setText(model.trackTime)
            trackTime.maxLines = 1
        }

    }
}