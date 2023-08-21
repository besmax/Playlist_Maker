package bes.max.playlistmaker.presentation.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { finish() }

        binding.settingsActivitySectionShare.setOnClickListener {
            viewModel.shareApp(getString(R.string.link_for_app_share))
        }

        binding.settingsActivitySectionSupport.setOnClickListener {
            sendEmailIntent()
        }

        binding.settingsActivitySectionAgreement.setOnClickListener {
            viewModel.openUserAgreement(getString(R.string.link_for_app_share))
        }

        viewModel.isNightModeActive.observe(this) {
            binding.settingsActivityThemeSwitcher.isChecked = it
        }

        binding.settingsActivityThemeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setIsNightModeActiveDebounce(checked)
        }

        viewModel.showingToast.observe(this) {
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
                Toast.makeText(this, getString(stringRes), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}