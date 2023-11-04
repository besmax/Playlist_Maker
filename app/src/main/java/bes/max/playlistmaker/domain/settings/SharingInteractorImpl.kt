package bes.max.playlistmaker.domain.settings

import bes.max.playlistmaker.domain.common.ExternalNavigator
import bes.max.playlistmaker.domain.models.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {

    override fun shareApp(link: String) {
        externalNavigator.shareApp(link)
    }

    override fun openUserAgreement(link: String) {
        externalNavigator.openUserAgreement(link)
    }

    override fun contactSupport(emailData: EmailData) {
        externalNavigator.sendEmail(emailData)
    }

}