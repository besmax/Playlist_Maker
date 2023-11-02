package bes.max.playlistmaker.presentation.mediateka.playlistdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.TrackListItemAdapter
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : BindingFragment<FragmentPlaylistDetailsBinding>() {


    private val playlistDetailsViewModel: PlaylistDetailsViewModel by viewModel()
    private var trackAdapter: TrackListItemAdapter? = null
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistDetailsBinding {
        return FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerview()

        binding.playlistDetailsScreenBackArrow.setOnClickListener {
            findNavController().navigateUp()
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
            trackAdapter!!.listOfTracks = state.playlist.tracks!!
        }

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
            }
        )
        binding.playlistDetailsScreenBottomSheetRecyclerView.adapter = trackAdapter

    }

    private fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

}