package bes.max.playlistmaker.presentation.mediateka.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import bes.max.playlistmaker.databinding.FragmentFavoriteTracksBinding
import bes.max.playlistmaker.presentation.mediateka.viewmodels.FavoriteTracksViewModel
import bes.max.playlistmaker.presentation.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteTracksBinding {
        return FragmentFavoriteTracksBinding.inflate(inflater, container, false)
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

}