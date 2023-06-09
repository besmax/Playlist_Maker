package bes.max.playlistmaker.ui.controllers

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.ActivityPlayerBinding
import bes.max.playlistmaker.domain.Player
import bes.max.playlistmaker.model.Track
import bes.max.playlistmaker.ui.PlayerViewModel
import bes.max.playlistmaker.ui.PlayerViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private val viewModel: PlayerViewModel by viewModels {
        PlayerViewModelFactory(
            fromJsonToTrack(
                (intent.getStringExtra(
                    getString(R.string.activity_search_to_activity_player_track_as_json)
                ))
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bind(viewModel.track)

        binding.activityPlayerBackArrow.setOnClickListener {
            finish()
        }

        viewModel.playerState.observe(this) {
            when (it) {
                Player.PlayerState.STATE_PLAYING -> {
                    binding.activityPlayerButtonPlay.isEnabled = true
                    binding.activityPlayerButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_player_pause))
                }

                Player.PlayerState.STATE_PAUSED -> {
                    binding.activityPlayerButtonPlay.isEnabled = true
                    binding.activityPlayerButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_player_play))
                }

                Player.PlayerState.STATE_PREPARED -> {
                    binding.activityPlayerButtonPlay.isEnabled = true
                    binding.activityPlayerButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_player_play))
                }

                else -> {
                    binding.activityPlayerButtonPlay.isEnabled = false
                    binding.activityPlayerButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_player_play))
                }
            }
        }

        viewModel.playingTime.observe(this) { playingTimeString ->
            binding.activityPlayerTimeCounter.text = playingTimeString
        }

        binding.activityPlayerButtonPlay.setOnClickListener {
            viewModel.playbackControl()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.playbackControl()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun fromJsonToTrack(json: String?): Track {
        return json.let {
            Gson().fromJson(json, Track::class.java)
        }
    }

    private fun bind(track: Track?) {
        if (track != null) {
            Glide.with(this)
                .load(track.bigCover)
                .placeholder(R.drawable.ic_picture_not_found)
                .transform(MultiTransformation(FitCenter(), RoundedCorners(8)))
                .into(binding.activityPlayerAlbumCover)

            with(binding) {
                activityPlayerTimeCounter.text = track.trackTime
                activityPlayerTrackName.text = track.trackName
                activityPlayerTrackAuthor.text = track.artistName
                activityPlayerDuration.text = track.trackTime
                activityPlayerAlbum.text = track.collectionName
                activityPlayerYear.text = track.year
                activityPlayerGenre.text = track.primaryGenreName
                activityPlayerCountry.text = track.country
            }
        }
    }

}