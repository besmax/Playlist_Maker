package bes.max.playlistmaker.presentation.mediateka.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentFavoriteTracksBinding
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.TrackListItemAdapter
import bes.max.playlistmaker.presentation.utils.BindingFragment
import bes.max.playlistmaker.presentation.utils.debounce
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    private var adapter: TrackListItemAdapter? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteTracksBinding {
        return FragmentFavoriteTracksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onElementClickAction = debounce<Track>(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
            action = { track ->
                findNavController().navigate(
                    R.id.action_mediatekaFragment_to_playerFragment,
                    Bundle().apply { putString("track", convertTrackToJson(track)) })
            }
        )
        adapter = TrackListItemAdapter(onElementClickAction)
        binding.favoriteScreenRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.favoriteScreenRecyclerView.adapter = adapter

        favoriteTracksViewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteScreenState.Content -> {
                    showContent(state.tracks)
                }

                is FavoriteScreenState.Empty -> {
                    showEmpty()
                }

                is FavoriteScreenState.Loading -> {
                    showLoading()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.getFavoriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null

    }

    private fun showContent(tracks: List<Track>) {
        with(binding) {
            adapter?.listOfTracks = tracks
            favoriteScreenRecyclerView.visibility = View.VISIBLE
            favoriteScreenPlaceholder.visibility = View.GONE
            favoriteScreenProgressBar.visibility = View.GONE
        }
    }

    private fun showEmpty() {
        with(binding) {
            favoriteScreenRecyclerView.visibility = View.GONE
            favoriteScreenPlaceholder.visibility = View.VISIBLE
            favoriteScreenProgressBar.visibility = View.GONE
        }
    }

    private fun showLoading() {
        with(binding) {
            favoriteScreenRecyclerView.visibility = View.GONE
            favoriteScreenPlaceholder.visibility = View.GONE
            favoriteScreenProgressBar.visibility = View.VISIBLE
        }
    }

    private fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoriteTracksFragment()
    }

}