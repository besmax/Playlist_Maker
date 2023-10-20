package bes.max.playlistmaker.presentation.mediateka.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.databinding.FragmentPlaylistBinding
import bes.max.playlistmaker.presentation.mediateka.MediatekaFragmentDirections
import bes.max.playlistmaker.presentation.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BindingFragment<FragmentPlaylistBinding>() {

    private val playlistViewModel: PlaylistViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentPlaylistPlaceholderButton.setOnClickListener {
            val action = MediatekaFragmentDirections.actionMediatekaFragmentToNewPlaylistFragment2()
            findNavController().navigate(action)
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}