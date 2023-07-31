package bes.max.playlistmaker.domain.settings

import bes.max.playlistmaker.domain.models.EmailData

interface SharingInteractor {

    fun shareApp(link: String)

    fun openUserAgreement(link: String)

    fun contactSupport(emailData: EmailData)

}