package bes.max.playlistmaker.presentation.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentMediatekaBinding
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaFragment : BindingFragment<FragmentMediatekaBinding>() {

    private lateinit var tabMediator: TabLayoutMediator

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediatekaBinding {
        return FragmentMediatekaBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentMediatekaViewpager.adapter =
            MediatekaViewPagerAdapter(this)
        tabMediator = TabLayoutMediator(
            binding.fragmentMediatekaTablayout,
            binding.fragmentMediatekaViewpager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.mediateka_screen_tab_title_favorite_tracks)
                1 -> tab.text = getString(R.string.mediateka_screen_tab_title_playlist)
            }
        }
        tabMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

}