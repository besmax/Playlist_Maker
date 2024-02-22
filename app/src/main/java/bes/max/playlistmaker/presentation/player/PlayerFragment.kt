package bes.max.playlistmaker.presentation.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentPlayerBinding
import bes.max.playlistmaker.domain.models.PlayerState
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.mediateka.playlists.PlaylistItemAdapter
import bes.max.playlistmaker.presentation.utils.BindingFragment
import bes.max.playlistmaker.presentation.utils.PlaybackButtonView.Companion.STATE_PAUSED
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : BindingFragment<FragmentPlayerBinding>() {

    private val safeArgs: PlayerFragmentArgs by navArgs()
    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(fromJsonToTrack(safeArgs.track))
    }

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private var playlistsAdapter: PlaylistItemAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind(playerViewModel.track)

        preparePlaylistRecyclerView()
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior?.addBottomSheetCallback(getCallbackForBottomSheetState())

        binding.playerScreenBackArrow.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        playerViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistsAdapter?.submitList(playlists)
        }

        playerViewModel.playerState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                PlayerState.STATE_PLAYING -> {
                    binding.playerScreenButtonPlay.isEnabled = true
                }

                PlayerState.STATE_PAUSED -> {
                    binding.playerScreenButtonPlay.isEnabled = true

                }

                PlayerState.STATE_PREPARED -> {
                    binding.playerScreenButtonPlay.isEnabled = true
                    binding.playerScreenButtonPlay.setState(STATE_PAUSED)
                }

                else -> {}
            }
        }

        playerViewModel.isPlaylistAdded.observe(viewLifecycleOwner) { isAddedPair ->
            if (isAddedPair.first == true) {
                showTrackAddedToast(isAddedPair.second)
                playerViewModel.clearIsPlaylistAdded()
            } else if ((isAddedPair.first == false)) {
                showTrackNotAddedToast(isAddedPair.second)
                playerViewModel.clearIsPlaylistAdded()
            }
        }

        playerViewModel.playingTime.observe(viewLifecycleOwner) { playingTimeString ->
            binding.playerScreenTimeCounter.text = playingTimeString
        }
        playerViewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.playerScreenButtonLike.setImageResource(
                if (isFavorite) R.drawable.ic_player_like_active
                else R.drawable.ic_player_like
            )
        }

        binding.playerScreenButtonPlay.setOnClickListener {
            playerViewModel.playbackControl()
        }

        binding.playerScreenButtonAdd.setOnClickListener {
            scaleAnimation(binding.playerScreenButtonAdd)
            playerViewModel.getPlaylists()
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.playerScreenButtonLike.setOnClickListener {
            if (playerViewModel.isFavorite.value == true) playerViewModel.deleteFromFavorite(
                playerViewModel.track
            ) else playerViewModel.addToFavorite(playerViewModel.track)

            scaleAnimation(binding.playerScreenButtonLike)
        }
        binding.playlistsBottomSheetButton.setOnClickListener {
            val trackArg = Gson().toJson(playerViewModel.track)
            val action =
                PlayerFragmentDirections.actionPlayerFragmentToNewPlaylistFragment(trackArg)
            findNavController().navigate(action)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("playlistName")
            ?.observe(viewLifecycleOwner) { playlistName ->
                showTrackAddedToast(playlistName)
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<String>("playlistName")
            }
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.getPlaylists()
    }

    override fun onStop() {
        super.onStop()
        if (playerViewModel.playerState.value == PlayerState.STATE_PLAYING) {
            playerViewModel.pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.releasePlayer()
        playlistsAdapter = null
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

    private fun getCallbackForBottomSheetState() =
        object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        binding.overlay.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.overlay.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }

    private fun preparePlaylistRecyclerView() {
        playlistsAdapter = PlaylistItemAdapter(
            listType = PlaylistItemAdapter.LINEAR_VERTICAL_LIST,
            doOnClick = { playlist ->
                playerViewModel.addTrackToPlaylist(playerViewModel.track, playlist)
            }
        )
        binding.playlistsBottomSheetRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.playlistsBottomSheetRecyclerView.adapter = playlistsAdapter
    }

    private fun showTrackAddedToast(playlistName: String) {
        val trackName = formatStringByLength(playerViewModel.track.trackName, 20)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        Toast.makeText(
            requireContext(),
            getString(R.string.player_screen_toast_added, trackName, playlistName),
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun showTrackNotAddedToast(playlistName: String) {
        if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_HIDDEN) {
            val trackName = formatStringByLength(playerViewModel.track.trackName, 20)
            Toast.makeText(
                requireContext(),
                getString(R.string.player_screen_toast_not_added, trackName, playlistName),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun formatStringByLength(text: String, maxLength: Int): String {
        return if (text.length > maxLength) playerViewModel.track.trackName.take(maxLength - 3) + "..."
        else text
    }


}