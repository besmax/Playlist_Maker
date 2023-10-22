package bes.max.playlistmaker.presentation.mediateka.playlist

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentPlaylistBinding
import bes.max.playlistmaker.presentation.mediateka.MediatekaFragmentDirections
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {

    private val playlistViewModel: PlaylistViewModel by viewModel()
    private var adapter: PlaylistItemAdapter? = PlaylistItemAdapter()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistScreenButton.setOnClickListener {
            val action = MediatekaFragmentDirections.actionMediatekaFragmentToNewPlaylistFragment2()
            findNavController().navigate(action)
        }
        binding.playlistScreenRecyclerView.adapter = adapter
        binding.playlistScreenRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        playlistViewModel.screenState.observe(viewLifecycleOwner) { state ->
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
        playlistViewModel.getPlaylists()
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
            lifecycleScope.launch {
                val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.playlist_screen_dialog)
                    .setTitle(getString(R.string.playlist_screen_dialog, playlistName))
                    .show()
                dialog.window?.setGravity(Gravity.BOTTOM)
                delay(2000)
                dialog.dismiss()
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<String>("playlistName")
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}