package bes.max.playlistmaker.presentation.mediateka

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import bes.max.playlistmaker.presentation.mediateka.fragments.FavoriteTracksFragment
import bes.max.playlistmaker.presentation.mediateka.fragments.PlaylistFragment

private const val FRAGMENT_QTY = 2

class MediatekaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = FRAGMENT_QTY

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}