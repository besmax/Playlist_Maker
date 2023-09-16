package bes.max.playlistmaker.presentation.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentPlayerBinding
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.TrackListItemAdapter
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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

        bind(playerViewModel.track)

        binding.playerScreenBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        playerViewModel.playerState.observe(viewLifecycleOwner) {
            when (it) {
                PlayerState.STATE_PLAYING -> {
                    binding.playerScreenButtonPlay.isEnabled = true
                    binding.playerScreenButtonPlay.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_player_pause))
                }

                PlayerState.STATE_PAUSED -> {
                    binding.playerScreenButtonPlay.isEnabled = true
                    binding.playerScreenButtonPlay.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_player_play))
                }

                PlayerState.STATE_PREPARED -> {
                    binding.playerScreenButtonPlay.isEnabled = true
                    binding.playerScreenButtonPlay.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_player_play))
                }

                else -> {
                    binding.playerScreenButtonPlay.isEnabled = false
                    binding.playerScreenButtonPlay.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_player_play))
                }
            }
        }

        playerViewModel.playingTime.observe(viewLifecycleOwner) { playingTimeString ->
            binding.playerScreenTimeCounter.text = playingTimeString
        }

        binding.playerScreenButtonPlay.setOnClickListener {
            playerViewModel.playbackControl()
        }

    }

    override fun onStop() {
        super.onStop()
        playerViewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playerViewModel.releasePlayer()
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