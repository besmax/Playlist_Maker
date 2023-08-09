package bes.max.playlistmaker.domain.settings

import bes.max.playlistmaker.domain.models.EmailData

interface ExternalNavigator {

    fun shareApp(link: String)

    fun openUserAgreement(link: String)

    fun sendEmail(emailData: EmailData)

}