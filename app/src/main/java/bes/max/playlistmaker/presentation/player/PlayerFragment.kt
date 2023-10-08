package bes.max.playlistmaker.presentation.player

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentPlayerBinding
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {

    private val safeArgs: PlayerFragmentArgs by navArgs()
    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(fromJsonToTrack(safeArgs.track))
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(requireActivity()) {
            requireViewById<BottomNavigationView>(R.id.bottom_navigation_view).isVisible = false
            requireViewById<View>(R.id.bottom_navigation_view_line_above).isVisible = false
        }

        bind(playerViewModel.track)

        binding.playerScreenBackArrow.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        playerViewModel.playerState.observe(viewLifecycleOwner) {
            when (it) {
                PlayerState.STATE_PLAYING -> {
                    binding.playerScreenButtonPlay.isEnabled = true
                }

                PlayerState.STATE_PAUSED -> {
                    binding.playerScreenButtonPlay.isEnabled = true
                }

                PlayerState.STATE_PREPARED -> {
                    binding.playerScreenButtonPlay.setImageResource(R.drawable.ic_player_play)
                    binding.playerScreenButtonPlay.isEnabled = true
                }

                else -> {
                    binding.playerScreenButtonPlay.setImageResource(R.drawable.ic_player_play)
                    binding.playerScreenButtonPlay.isEnabled = false
                }
            }
        }

        playerViewModel.playingTime.observe(viewLifecycleOwner) { playingTimeString ->
            binding.playerScreenTimeCounter.text = playingTimeString
        }
        playerViewModel.isFavorite.observe(viewLifecycleOwner) {
            binding.playerScreenButtonLike.setImageResource(
                if (it) R.drawable.ic_player_like_active
                else R.drawable.ic_player_like
            )
        }

        binding.playerScreenButtonPlay.setOnClickListener {
            playerViewModel.playbackControl()
            playPauseAnimation()
        }

        binding.playerScreenButtonLike.setOnClickListener {
            if (playerViewModel.isFavorite.value == true) playerViewModel.deleteFromFavorite(
                playerViewModel.track
            ) else playerViewModel.addToFavorite(playerViewModel.track)

            scaleAnimation(binding.playerScreenButtonLike)
        }

    }

    override fun onStop() {
        super.onStop()
        if (playerViewModel.playerState.value == PlayerState.STATE_PLAYING) {
            playerViewModel.pausePlayer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playerViewModel.releasePlayer()
    }

    private fun scaleAnimation(view: View) {
        val animIncrease = ScaleAnimation(
            1f, 1.1f,
            1f, 1.1f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 1f
        ).apply {
            fillAfter = true
            duration = 250
        }

        val animDecrease = ScaleAnimation(
            1.1f, 1f,
            1.1f, 1f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 1f
        ).apply {
            fillAfter = true
            duration = 250
        }

        view.startAnimation(animIncrease)
        view.startAnimation(animDecrease)
    }

    private fun playPauseAnimation() {
        if (playerViewModel.playerState.value == PlayerState.STATE_PLAYING) {
            binding.playerScreenButtonPlay.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.avd_play_to_pause
                )
            )
        } else {
            binding.playerScreenButtonPlay.setImageDrawable(
                getDrawable(
                    requireContext(),
                    R.drawable.avd_pause_to_play
                )
            )
        }
        val avdPlayToPause = binding.playerScreenButtonPlay.drawable as AnimatedVectorDrawable
        avdPlayToPause.start()
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
                .transform(MultiTransformation(RoundedCorners(roundedCorner)))
                .into(binding.playerScreenAlbumCover)

            with(binding) {
                playerScreenTimeCounter.text = track.trackTime
                playerScreenTrackName.text = track.trackName
                playerScreenTrackAuthor.text = track.artistName
                playerScreenDuration.text = track.trackTime
                playerScreenAlbum.text = track.collectionName
                playerScreenYear.text = track.year
                playerScreenGenre.text = track.primaryGenreName
                playerScreenCountry.text = track.country
            }
        }
    }


}