package bes.max.playlistmaker.presentation.mediateka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentFavoriteTracksBinding
import bes.max.playlistmaker.presentation.utils.BindingFragment

class FavoriteTracksFragment : BindingFragment<FragmentFavoriteTracksBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteTracksBinding {
        return FragmentFavoriteTracksBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_tracks, container, false)
    }

}