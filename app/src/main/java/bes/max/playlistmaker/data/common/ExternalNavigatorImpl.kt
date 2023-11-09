package bes.max.playlistmaker.data.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import bes.max.playlistmaker.domain.common.ExternalNavigator
import bes.max.playlistmaker.domain.models.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareApp(link: String) {
        val shareAppIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        startActivity(context, shareAppIntent, null)
    }

    override fun openUserAgreement(link: String) {
        val openUserAgreementIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(link)
        )
        openUserAgreementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, openUserAgreementIntent, null)
    }

    override fun sendEmail(emailData: EmailData) {
        val sendEmailIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        startActivity(context, sendEmailIntent, null)
    }

    override fun sharePlaylist(playlistString: String) {
        val sharePlaylistIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, playlistString)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        startActivity(context, sharePlaylistIntent, null)
    }


}