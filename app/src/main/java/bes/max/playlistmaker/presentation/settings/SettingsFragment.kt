package bes.max.playlistmaker.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import bes.max.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
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
                    SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showingToast.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun showToast(stringRes: Int) {
        if (stringRes != 0) {
            try {
                Toast.makeText(requireContext(), getString(stringRes), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}