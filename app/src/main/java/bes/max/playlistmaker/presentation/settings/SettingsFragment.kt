package bes.max.playlistmaker.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import bes.max.playlistmaker.R
import bes.max.playlistmaker.presentation.utils.setClickListenersForAllViews
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Text(text = "Hello world.")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsScreenSectionShare.setClickListenersForAllViews {
            viewModel.shareApp(getString(R.string.link_for_app_share))
        }

        binding.settingsScreenSectionSupport.setClickListenersForAllViews {
            sendEmailIntent()
        }

        binding.settingsScreenSectionAgreement.setClickListenersForAllViews {
            viewModel.openUserAgreement(getString(R.string.link_for_app_share))
        }

        viewModel.isNightModeActive.observe(viewLifecycleOwner) {
            binding.settingsScreenThemeSwitcher.isChecked = it
        }

        binding.settingsScreenThemeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setIsNightModeActiveDebounce(checked)
        }

        viewModel.showingToast.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun sendEmailIntent() {
        viewModel.contactSupport(
            getString(R.string.email_for_support),
            getString(R.string.email_theme_for_support),
            getString(R.string.email_text_for_support)
        )
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