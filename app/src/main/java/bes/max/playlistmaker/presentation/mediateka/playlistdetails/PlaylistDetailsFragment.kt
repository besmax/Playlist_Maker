package bes.max.playlistmaker.presentation.mediateka.playlistdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.doOnLayout
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.TrackListItemAdapter
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : BindingFragment<FragmentPlaylistDetailsBinding>() {


    private val playlistDetailsViewModel: PlaylistDetailsViewModel by viewModel()
    private var trackAdapter: TrackListItemAdapter? = null
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistDetailsBinding {
        return FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistDetailsScreenConstraintLayout.doOnLayout {
            setUpBottomsheet()
        }

        setUpBottomsheetMenu()

        setUpRecyclerview()

        binding.playlistDetailsScreenBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playlistDetailsScreenShare.setNavigationOnClickListener {
            sharePlaylist()
        }
        binding.playlistDetailsScreenMenu.setOnClickListener {
            playlistDetailsViewModel.showMenu()
        }

        binding.playlistDetailsScreenBottomSheetMenuDelete.setOnClickListener {
            showDeletePlaylistDialog()
        }

        binding.playlistDetailsScreenBottomSheetMenuShare.setOnClickListener {
            sharePlaylist()
        }
        binding.playlistDetailsScreenBottomSheetMenuEdit.setOnClickListener {
            val playlistId =
                if (playlistDetailsViewModel.screenState.value is PlaylistDetailsScreenState.Menu)
                    (playlistDetailsViewModel.screenState.value as PlaylistDetailsScreenState.Menu).playlist.id
                else (playlistDetailsViewModel.screenState.value as PlaylistDetailsScreenState.Content).playlistDetails.playlist.id
            val action =
                PlaylistDetailsFragmentDirections.actionPlaylistDetailsFragmentToEditPlaylistFragment(
                    playlistId
                )
            bottomSheetBehaviorMenu?.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(action)
        }

        playlistDetailsViewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistDetailsScreenState.Content -> showContent(state)
                is PlaylistDetailsScreenState.Menu -> showMenu(state)
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        playlistDetailsViewModel.getPlaylist()
    }

    private fun showContent(state: PlaylistDetailsScreenState.Content) {
        if (trackAdapter != null) {
            trackAdapter!!.listOfTracks = state.playlistDetails.tracks
        }

        if (state.playlistDetails.tracks.isEmpty()) {
            binding.playlistDetailsScreenBottomSheetNoTracks.visibility = View.VISIBLE
        } else {
            binding.playlistDetailsScreenBottomSheetNoTracks.visibility = View.GONE
        }

        if (!state.playlistDetails.cover.isNullOrBlank()) {
            binding.playlistDetailsScreenCover.setImageURI(state.playlistDetails.cover.toUri())
            binding.playlistDetailsScreenCover.setPadding(0, 0, 0, 0)
        }

        with(binding) {
            playlistDetailsScreenTitle.text = state.playlistDetails.title
            playlistDetailsScreenDescription.text = state.playlistDetails.description
            playlistDetailsScreenDuration.text =
                requireContext().resources.getQuantityString(
                    R.plurals.minutes_number,
                    state.playlistDetails.duration,
                    state.playlistDetails.duration
                )
            playlistDetailsScreenTracksNumber.text =
                requireContext().resources.getQuantityString(
                    R.plurals.tracks_number,
                    state.playlistDetails.tracksNumber,
                    state.playlistDetails.tracksNumber
                )
        }
        //bottomSheetBehaviorMenu?.state = BottomSheetBehavior.STATE_HIDDEN
        bindPlaylistItemViews(state.playlistDetails.playlist)
    }

    private fun showMenu(state: PlaylistDetailsScreenState.Menu) {
        bottomSheetBehaviorMenu?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun showDeleteTrackDialog(trackId: Long) {
        val alert = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
            .setTitle(R.string.playlistdetails_screen_dialog_title)
            .setMessage(R.string.playlistdetails_screen_dialog_message)
            .setNegativeButton(R.string.Cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.delete) { dialog, _ ->
                playlistDetailsViewModel.deleteTrackFromPlaylist(trackId)
                dialog.dismiss()
            }
        alert.show()
    }

    private fun showDeletePlaylistDialog() {
        val alert = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
            .setTitle(R.string.delete_playlist)
            .setMessage(R.string.want_delete_playlist)
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.yes) { dialog, _ ->
                playlistDetailsViewModel.deletePlaylist(
                    (playlistDetailsViewModel.screenState.value as PlaylistDetailsScreenState.Menu)
                        .playlist
                )
                dialog.dismiss()
                findNavController().navigateUp()
            }
        alert.show()
    }

    private fun setUpRecyclerview() {
        trackAdapter = TrackListItemAdapter(
            onListElementClick = { track ->
                val trackArg = convertTrackToJson(track)
                val action =
                    PlaylistDetailsFragmentDirections.actionPlaylistDetailsFragmentToPlayerFragment(
                        trackArg
                    )
                findNavController().navigate(action)
            },
            onListElementLongClick = { trackId ->
                showDeleteTrackDialog(trackId)
            },
            coverOption = TrackListItemAdapter.SMALL_COVER_OPTION
        )
        binding.playlistDetailsScreenBottomSheetRecyclerView.adapter = trackAdapter

    }

    private fun setUpBottomsheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistDetailsScreenBottomSheet)
        val availableHeight =
            binding.playlistDetailsScreenCoordinatorLayout.height - binding.playlistDetailsScreenConstraintLayout.height
        bottomSheetBehavior!!.setPeekHeight(availableHeight, false)
    }

    private fun setUpBottomsheetMenu() {
        bottomSheetBehaviorMenu =
            BottomSheetBehavior.from(binding.playlistDetailsScreenBottomSheetMenu).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetBehaviorMenu!!.addBottomSheetCallback(getCallbackForBottomSheetState())
    }

    private fun bindPlaylistItemViews(playlist: Playlist) {
        with(binding) {
            Glide.with(playlistDetailsScreenBottomSheetMenuPlaylist.playlistCardview)
                .load(playlist.coverPath)
                .transform(
                    RoundedCorners(
                        resources.getDimensionPixelSize(
                            R.dimen.search_activity_album_cover_corner_radius
                        )
                    )
                )
                .placeholder(R.drawable.ic_picture_not_found)
                .into(playlistDetailsScreenBottomSheetMenuPlaylist.playlistCover)
            playlistDetailsScreenBottomSheetMenuPlaylist.playlistName.text = playlist.name
            playlistDetailsScreenBottomSheetMenuPlaylist.tracksQty.text =
                requireContext().resources.getQuantityString(
                    R.plurals.tracks_number,
                    playlist.tracksNumber,
                    playlist.tracksNumber
                )
        }
    }

    private fun sharePlaylist() {
        if (playlistDetailsViewModel.screenState.value is PlaylistDetailsScreenState.Content) {
            if ((playlistDetailsViewModel.screenState.value as PlaylistDetailsScreenState.Content).playlistDetails.tracks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    R.string.playlistdetails_screen_nothing_share,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                playlistDetailsViewModel.sharePlaylist(
                    (playlistDetailsViewModel.screenState.value as PlaylistDetailsScreenState.Content).playlistDetails.playlist
                )
            }
        } else if (playlistDetailsViewModel.screenState.value is PlaylistDetailsScreenState.Menu) {
            if ((playlistDetailsViewModel.screenState.value as PlaylistDetailsScreenState.Menu).playlist.tracks?.isEmpty() == true) {
                Toast.makeText(
                    requireContext(),
                    R.string.playlistdetails_screen_nothing_share,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                playlistDetailsViewModel.sharePlaylist(
                    (playlistDetailsViewModel.screenState.value as PlaylistDetailsScreenState.Menu).playlist
                )
            }


        }
    }

    private fun getCallbackForBottomSheetState() =
        object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED,
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        playlistDetailsViewModel.onCloseMenu()
                    }

                    else -> {
                        binding.overlay.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        }

    private fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

}