package bes.max.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { finish() }

        binding.settingsActivitySectionShare.setOnClickListener {
            startActivity(shareAppLinkIntent())
        }

        binding.settingsActivitySectionSupport.setOnClickListener {
            startActivity(sendEmailIntent())
        }

        binding.settingsActivitySectionAgreement.setOnClickListener {
            val openAgreementIntent = openUserAgreementIntent()
            if (openAgreementIntent.resolveActivity(packageManager) != null)
                startActivity(openAgreementIntent)
        }

        viewModel.isNightModeActive.observe(this) {
            binding.settingsActivityThemeSwitcher.isChecked = it
        }

        binding.settingsActivityThemeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.setIsNightModeActive(checked)
        }
    }

    private fun shareAppLinkIntent(): Intent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.link_for_app_share))
            type = "text/plain"
            Intent.createChooser(this, null)
        }

    private fun sendEmailIntent(): Intent =
        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_for_support)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme_for_support))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text_for_support))
            Intent.createChooser(this, null)
        }

    private fun openUserAgreementIntent(): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(getString(R.string.link_for_app_share))
    )
}