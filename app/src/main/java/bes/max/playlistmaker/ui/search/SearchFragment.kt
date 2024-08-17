package bes.max.playlistmaker.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.presentation.search.SearchViewModel
import bes.max.playlistmaker.presentation.settings.SettingsViewModel
import bes.max.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val darkTheme by settingsViewModel.isNightModeActive.observeAsState(initial = isSystemInDarkTheme())
                PlaylistMakerTheme(darkTheme = darkTheme) {
                    SearchScreen(navController = findNavController(), viewModel = searchViewModel)
                }
            }
        }
    }

}