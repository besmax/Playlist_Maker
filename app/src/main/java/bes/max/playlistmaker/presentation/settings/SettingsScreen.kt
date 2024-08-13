package bes.max.playlistmaker.presentation.settings


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.playlistmaker.ui.theme.ysDisplayFamily


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val showToast by viewModel.showingToast.observeAsState()
    val isNightModeActive by viewModel.isNightModeActive.observeAsState()


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
    ) {

        Text(
            text = title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )

    }

}