package bes.max.playlistmaker.domain.common

import bes.max.playlistmaker.domain.models.EmailData

interface ExternalNavigator {

    fun shareApp(link: String)

    fun openUserAgreement(link: String)

    fun sendEmail(emailData: EmailData)

    fun sharePlaylist(playlistString: String)

}