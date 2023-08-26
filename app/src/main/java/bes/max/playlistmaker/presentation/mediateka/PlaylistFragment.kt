package bes.max.playlistmaker.presentation.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bes.max.playlistmaker.R
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    companion object {

        fun newInstance() = PlaylistFragment()

    }

}