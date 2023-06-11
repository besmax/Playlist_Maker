package bes.max.playlistmaker.ui.controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.ActivityPlayerBinding
import bes.max.playlistmaker.model.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val jsonString = intent.getStringExtra(
            getString(R.string.activity_search_to_activity_player_track_as_json))
        val track = jsonString?.let { fromJsonToTrack(it) }
        bind(track)

        binding.activityPlayerBackArrow.setOnClickListener {
            finish()
        }
    }


    private fun fromJsonToTrack(json: String): Track =
        Gson().fromJson(json, Track::class.java)


    private fun bind(track: Track?) {
        if (track != null) {
            Glide.with(this)
                .load(track.bigCover)
                .placeholder(R.drawable.ic_picture_not_found)
                .transform(RoundedCorners(8), CenterCrop())
                .into(binding.activityPlayerAlbumCover)

            binding.activityPlayerTimeCounter.text = track.trackTime
            binding.activityPlayerTrackName.text = track.trackName
            binding.activityPlayerTrackAuthor.text = track.artistName
            binding.activityPlayerDuration.text = track.trackTime
            binding.activityPlayerAlbum.text = track.collectionName
            binding.activityPlayerYear.text = track.year
            binding.activityPlayerGenre.text = track.primaryGenreName
            binding.activityPlayerCountry.text = track.country
        }
    }
}