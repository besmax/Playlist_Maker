package bes.max.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backIcon = findViewById<ImageView>(R.id.back_icon)
        backIcon.setOnClickListener { finish() }

        val shareIcon = findViewById<ImageView>(R.id.settings_activity_icon_share)
        shareIcon.setOnClickListener {
            startActivity(shareAppLinkIntent())
        }

        val supportIcon = findViewById<ImageView>(R.id.settings_activity_icon_support)
        supportIcon.setOnClickListener {
            val sendEmailIntent = sendEmailIntent()
            if (sendEmailIntent.resolveActivity(packageManager) != null)
                startActivity(sendEmailIntent)
        }

        val agreementIcon = findViewById<ImageView>(R.id.settings_activity_icon_agreement)
        agreementIcon.setOnClickListener {
            val openAgreementIntent = openUserAgreementIntent()
            if (openAgreementIntent.resolveActivity(packageManager) != null)
                startActivity(openAgreementIntent)
        }
    }

    private fun shareAppLinkIntent(): Intent {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.link_for_app_share))
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, null)
    }

    private fun sendEmailIntent(): Intent = Intent().apply {
        action = Intent.ACTION_SENDTO
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_for_support)))
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme_for_support))
        putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text_for_support))
    }

    private fun openUserAgreementIntent(): Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(getString(R.string.link_for_app_share))
    )

}