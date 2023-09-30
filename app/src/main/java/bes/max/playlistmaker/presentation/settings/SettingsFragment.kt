package bes.max.playlistmaker.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentSearchBinding
import bes.max.playlistmaker.databinding.FragmentSettingsBinding
import bes.max.playlistmaker.presentation.utils.BindingFragment
import bes.max.playlistmaker.presentation.utils.setClickListenersForAllViews
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(requireActivity()) {
            requireViewById<BottomNavigationView>(R.id.bottom_navigation_view).isVisible = true
            requireViewById<View>(R.id.bottom_navigation_view_line_above).isVisible = true
        }



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