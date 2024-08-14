package bes.max.playlistmaker.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.SearchScreenState
import bes.max.playlistmaker.presentation.search.SearchViewModel
import bes.max.playlistmaker.presentation.settings.SettingsViewModel
import bes.max.playlistmaker.presentation.utils.debounce
import bes.max.playlistmaker.ui.theme.PlaylistMakerTheme
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val darkTheme by settingsViewModel.isNightModeActive.observeAsState(initial = isSystemInDarkTheme())
                PlaylistMakerTheme(darkTheme = darkTheme) {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onElementClickAction = debounce<Track>(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
            action = { track ->
                searchViewModel.saveTrackToHistory(track)
                val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(convertTrackToJson(track))
                findNavController().navigate(action)
            }
        )



    }

    private fun showScreenContent(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Default -> {
                showDefaultScreenState()
            }

            is SearchScreenState.Loading -> {
                showLoading()
            }

            is SearchScreenState.History -> {
                adapterForHistory.listOfTracks = SearchScreenState.History.tracks
                showHistory()
            }

            is SearchScreenState.Tracks -> {
                adapter.listOfTracks = SearchScreenState.Tracks.tracks
                showTracks()
            }

            is SearchScreenState.TracksNotFound -> {
                showNotFound()
            }

            is SearchScreenState.SearchError -> {
                showError()
            }
        }
    }

    private fun showDefaultScreenState() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.GONE
            searchScreenPlaceholderButton.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun showLoading() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.GONE
            searchScreenProgressBar.visibility = View.VISIBLE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun showHistory() {
        with(binding) {
            searchScreenHistoryGroup.visibility =
                if (adapterForHistory.listOfTracks.isNotEmpty()) View.VISIBLE
                else View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenPlaceholder.visibility = View.GONE
        }
    }

    private fun showTracks() {
        with(binding) {
            searchScreenRecyclerViewTracks.visibility = View.VISIBLE
            searchScreenHistoryGroup.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenPlaceholder.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.VISIBLE
            searchScreenPlaceholderImage.setImageResource(R.drawable.img_not_found)
            searchScreenPlaceholderText.setText(getString(R.string.search_screen_placeholder_text_not_found))
            searchScreenPlaceholderButton.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun showError() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.VISIBLE
            searchScreenPlaceholderImage.setImageResource(R.drawable.img_no_internet)
            searchScreenPlaceholderText.setText(getString(R.string.search_screen_placeholder_text_error))
            searchScreenPlaceholderButton.visibility = View.VISIBLE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}