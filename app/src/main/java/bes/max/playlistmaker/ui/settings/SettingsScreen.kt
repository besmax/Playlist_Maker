package bes.max.playlistmaker.ui.settings


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.playlistmaker.R
import bes.max.playlistmaker.presentation.settings.SettingsViewModel
import bes.max.playlistmaker.ui.theme.ysDisplayFamily


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val showToast by viewModel.showingToast.observeAsState()
    val isNightModeActive by viewModel.isNightModeActive.observeAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val shareUrl = stringResource(R.string.link_for_app_share)

        SettingSection(
            onSectionClick = { viewModel.shareApp(shareUrl) },
            iconResId = R.drawable.ic_share,
            title = stringResource(id = R.string.share_app)
        )

        val email = stringResource(R.string.email_for_support)
        val emailSubject = stringResource(R.string.email_theme_for_support)
        val emailText = stringResource(R.string.email_text_for_support)

        SettingSection(
            onSectionClick = {
                viewModel.contactSupport(email, emailSubject, emailText)
            },
            iconResId = R.drawable.ic_support,
            title = stringResource(id = R.string.contact_support)
        )

        val agreementUrl = stringResource(R.string.link_for_app_share)

        SettingSection(
            onSectionClick = {
                viewModel.openUserAgreement(agreementUrl)
            },
            iconResId = R.drawable.ic_support,
            title = stringResource(id = R.string.contact_support)
        )
    }

}

@Composable
fun SettingSection(
    onSectionClick: () -> Unit,
    iconResId: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .clickable { onSectionClick }
    ) {

        Text(
            text = title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )

        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "settings icon $title",
            tint = Color.Unspecified
        )

    }

}