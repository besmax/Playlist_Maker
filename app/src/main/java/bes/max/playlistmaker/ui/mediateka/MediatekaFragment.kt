package bes.max.playlistmaker.ui.mediateka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import bes.max.playlistmaker.presentation.settings.SettingsViewModel
import bes.max.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaFragment : Fragment() {

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

                }
            }
        }
    }

}