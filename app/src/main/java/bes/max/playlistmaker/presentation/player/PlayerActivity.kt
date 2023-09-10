package bes.max.playlistmaker.presentation.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.navArgument
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.ActivityPlayerBinding
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.models.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(
            fromJsonToTrack(
                intent.getStringExtra(ARGS_TRACK)
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
                PlayerState.STATE_PLAYING -> {
                    binding.activityPlayerButtonPlay.isEnabled = true
                    binding.activityPlayerButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_player_pause))
                }

                PlayerState.STATE_PAUSED -> {
                    binding.activityPlayerButtonPlay.isEnabled = true
                    binding.activityPlayerButtonPlay.setImageDrawable(getDrawable(R.drawable.ic_player_play))
                }

                PlayerState.STATE_PREPARED -> {
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
        viewModel.pausePlayer()
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
        val roundedCorner =
            resources.getDimensionPixelSize(R.dimen.activity_player_album_cover_corner_radius)
        if (track != null) {
            Glide.with(this)
                .load(track.bigCover)
                .placeholder(R.drawable.ic_picture_not_found)
                .transform(MultiTransformation(FitCenter(), RoundedCorners(roundedCorner)))
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

    companion object {
        private const val ARGS_TRACK = "args_track"
        fun createTrackArg(track: String): Bundle =
            bundleOf(ARGS_TRACK to track)
    }

}