package bes.max.playlistmaker.presentation.mediateka.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import bes.max.playlistmaker.databinding.FragmentPlaylistBinding
import bes.max.playlistmaker.presentation.mediateka.viewmodels.PlaylistViewModel
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

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}