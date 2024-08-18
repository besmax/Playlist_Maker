package bes.max.playlistmaker.ui.mediateka.favorite

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.mediateka.favorite.FavoriteScreenState
import bes.max.playlistmaker.presentation.mediateka.favorite.FavoriteTracksViewModel
import bes.max.playlistmaker.ui.common.Loading
import bes.max.playlistmaker.ui.common.TrackList
import bes.max.playlistmaker.ui.mediateka.MediatekaFragmentDirections
import bes.max.playlistmaker.ui.theme.ysDisplayFamily
import com.google.gson.Gson
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteTracksScreen(
    navController: NavController,
    favoriteViewModel: FavoriteTracksViewModel = koinViewModel()
) {

    val uiState by favoriteViewModel.screenState.observeAsState(initial = FavoriteScreenState.Empty)

    LaunchedEffect(key1 = Unit) {
        favoriteViewModel.getFavoriteTracks()
    }

    FavoriteTracksScreenContent(
        uiState = uiState,
        onItemClick = { track ->
            val trackArg = Gson().toJson(track)
            val encodeTrackArg = Uri.encode(trackArg)
            val action =
                MediatekaFragmentDirections.actionMediatekaFragmentToPlayerFragment(encodeTrackArg)
            navController.navigate(action)
        }
    )

}

@Composable
fun FavoriteTracksScreenContent(
    uiState: FavoriteScreenState,
    onItemClick: (Track) -> Unit
) {

    when (uiState) {
        is FavoriteScreenState.Empty -> EmptyFavoriteTracks()
        is FavoriteScreenState.Loading -> Loading()
        is FavoriteScreenState.Content -> TrackList(
            tracks = uiState.tracks,
            onItemClick = onItemClick,
            isReverse = false
        )
    }

}

@Composable
fun EmptyFavoriteTracks() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_not_found),
            contentDescription = "Favorite tracks not found",
        )
        Text(
            text = stringResource(id = R.string.favorite_tracks_screen_placeholder_text),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
