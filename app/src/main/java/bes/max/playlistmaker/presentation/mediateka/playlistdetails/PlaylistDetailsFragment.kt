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
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.TrackListItemAdapter
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : BindingFragment<FragmentPlaylistDetailsBinding>() {


    private val playlistDetailsViewModel: PlaylistDetailsViewModel by viewModel()
    private var trackAdapter: TrackListItemAdapter? = null
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

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

        setUpRecyclerview()

        binding.playlistDetailsScreenBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playlistDetailsScreenShare.setNavigationOnClickListener {
            sharePlaylist()
        }

        playlistDetailsViewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistDetailsScreenState.Content -> showContent(state)
                else -> {}
            }
        }
    }

    private fun showContent(state: PlaylistDetailsScreenState.Content) {
        if (trackAdapter != null) {
            trackAdapter!!.listOfTracks = state.playlistDetails.tracks
        }
        if (!state.playlistDetails.cover.isNullOrBlank()) {
            binding.playlistDetailsScreenCover.setImageURI(state.playlistDetails.cover.toUri())
            binding.playlistDetailsScreenCover.setPadding(0, 0, 0, 0)
        }

        with(binding) {
            playlistDetailsScreenTitle.text = state.playlistDetails.title
            playlistDetailsScreenDescription.text = state.playlistDetails.description
            playlistDetailsScreenDuration.text =
                getString(R.string.number_of_minutes, state.playlistDetails.duration)
            playlistDetailsScreenTracksNumber.text =
                getString(R.string.number_of_tracks, state.playlistDetails.tracksNumber.toString())
        }

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
            }
        )
        binding.playlistDetailsScreenBottomSheetRecyclerView.adapter = trackAdapter

    }

    private fun setUpBottomsheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistDetailsScreenBottomSheet)
        val availableHeight =
            binding.playlistDetailsScreenCoordinatorLayout.height - binding.playlistDetailsScreenConstraintLayout.height
        bottomSheetBehavior!!.setPeekHeight(availableHeight, false)
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
        }

    }

    private fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

}