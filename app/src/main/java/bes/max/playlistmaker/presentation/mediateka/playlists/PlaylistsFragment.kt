package bes.max.playlistmaker.presentation.mediateka.playlists

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentPlaylistBinding
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistBinding>() {

    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private var adapter: PlaylistItemAdapter? =
        PlaylistItemAdapter(
            listType = PlaylistItemAdapter.GRID_LIST,
            doOnClick = { playlist ->
                findNavController().navigate(
                    R.id.action_mediatekaFragment_to_playlistDetailsFragment,
                    Bundle().apply { putLong("playlistId", playlist.id) }
                )
            }
        )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistScreenButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediatekaFragment_to_newPlaylistFragment2
            )
        }
        binding.playlistScreenRecyclerView.adapter = adapter
        binding.playlistScreenRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        playlistsViewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistScreenState.Empty -> showEmpty()

                is PlaylistScreenState.Content -> showContent(state)

                is PlaylistScreenState.Loading -> showLoading()
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("playlistName")
            ?.observe(viewLifecycleOwner) { playlistName ->
                showPlaylistCreatedMessage(playlistName)
            }
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.getPlaylists()
    }

    private fun showEmpty() {
        with(binding) {
            playlistScreenPlaceholder.visibility = View.VISIBLE
            playlistScreenRecyclerView.visibility = View.GONE
            playlistScreenProgressBar.visibility = View.GONE
        }
    }

    private fun showContent(state: PlaylistScreenState.Content) {
        with(binding) {
            playlistScreenPlaceholder.visibility = View.GONE
            playlistScreenRecyclerView.visibility = View.VISIBLE
            playlistScreenProgressBar.visibility = View.GONE
        }
        adapter?.submitList(state.playlists)
    }

    private fun showLoading() {
        with(binding) {
            playlistScreenPlaceholder.visibility = View.GONE
            playlistScreenRecyclerView.visibility = View.GONE
            playlistScreenProgressBar.visibility = View.VISIBLE
        }
    }

    private fun showPlaylistCreatedMessage(playlistName: String) {
        if (!playlistName.isNullOrBlank()) {
            val snackbar = Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.playlist_screen_dialog, playlistName),
                Snackbar.LENGTH_LONG
            )
            val view = snackbar.view
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black_white))
            val textView =
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView.ellipsize = TextUtils.TruncateAt.END
            snackbar.show()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<String>("playlistName")

        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}