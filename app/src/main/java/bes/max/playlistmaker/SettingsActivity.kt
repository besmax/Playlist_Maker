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
        backIcon.setOnClickListener {
            finish()
        }

        val shareIcon = findViewById<ImageView>(R.id.settings_activity_icon_share)
        shareIcon.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_for_app_share))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        val supportIcon = findViewById<ImageView>(R.id.settings_activity_icon_support)
        supportIcon.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_for_support)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme_for_support))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text_for_support))
            }
            //if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)

        }

        val agreementIcon = findViewById<ImageView>(R.id.settings_activity_icon_agreement)
        agreementIcon.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.link_for_app_share))
            )
            //if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)
        }

    }

}