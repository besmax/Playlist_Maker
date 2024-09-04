package bes.max.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.SearchScreenState
import bes.max.playlistmaker.presentation.search.SearchViewModel
import bes.max.playlistmaker.presentation.utils.debounce
import bes.max.playlistmaker.ui.common.Loading
import bes.max.playlistmaker.ui.common.Title
import bes.max.playlistmaker.ui.common.TrackList
import bes.max.playlistmaker.ui.theme.YpBlack
import bes.max.playlistmaker.ui.theme.YpLightGray
import bes.max.playlistmaker.ui.theme.ysDisplayFamily
import com.google.gson.Gson
import org.koin.androidx.compose.koinViewModel

private const val CLICK_DEBOUNCE_DELAY = 1000L

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel()
) {

    val uiScreenState by viewModel.screenState.observeAsState(SearchScreenState.Default)
    val coroutineScope = rememberCoroutineScope()

    val onElementClickAction = debounce<Track>(
        delayMillis = CLICK_DEBOUNCE_DELAY,
        coroutineScope = coroutineScope,
        useLastParam = false,
        action = { track ->
            viewModel.saveTrackToHistory(track)
            val action =
                SearchFragmentDirections.actionSearchFragmentToPlayerFragment(Gson().toJson(track))
            navController.navigate(action)
        }
    )

    SearchScreenContent(
        uiScreenState = uiScreenState,
        onInputChange = { input -> viewModel.searchDebounce(input) },
        onElementClickAction = onElementClickAction,
        clearHistory = viewModel::clearHistory,
        refresh = viewModel::refreshSearch
    )

}

@Composable
fun SearchScreenContent(
    uiScreenState: SearchScreenState,
    onInputChange: (String) -> Unit,
    onElementClickAction: (Track) -> Unit,
    clearHistory: () -> Unit,
    refresh: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        Title(text = stringResource(id = R.string.search))
        SearchInput(onInputChange = onInputChange)

        when (uiScreenState) {
            is SearchScreenState.Default -> {}

            is SearchScreenState.Loading -> {
                Loading()
            }

            is SearchScreenState.History -> {
                TrackHistory(
                    tracks = uiScreenState.tracks,
                    onTrackClick = onElementClickAction,
                    clearHistory = clearHistory,
                )
            }

            is SearchScreenState.Tracks -> {
                TrackList(
                    tracks = uiScreenState.tracks,
                    onItemClick = onElementClickAction,
                    isReverse = false
                )
            }

            is SearchScreenState.TracksNotFound -> {
                NotFound()
            }

            is SearchScreenState.SearchError -> {
                ConnectionProblem(refresh)
            }
        }
    }

}

@Composable
fun SearchInput(onInputChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = {
            text = it
            onInputChange(it)
        },
        label = { if (text.isBlank()) Text(text = stringResource(id = R.string.search)) },
        maxLines = 1,
        textStyle = TextStyle(
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = YpLightGray,
            unfocusedContainerColor = YpLightGray,
            disabledContainerColor = YpLightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = YpBlack,
            unfocusedTextColor = YpBlack
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_find),
                contentDescription = "search icon",
            )
        },
        trailingIcon = {
            if (text.isNotBlank()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clear_text),
                    contentDescription = "clear text icon",
                    modifier = Modifier
                        .clickable {
                            text = ""
                            onInputChange("")
                        }
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .onFocusChanged {
                if (it.hasFocus && text.isBlank()) {
                    onInputChange(text)
                }
            }
    )
}

@Composable
fun TrackHistory(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit,
    clearHistory: () -> Unit
) {
    if (tracks.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.you_searched),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )

        TrackList(tracks = tracks, onItemClick = onTrackClick, isReverse = true)

        Button(
            onClick = { clearHistory() },
            modifier = Modifier
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = stringResource(id = R.string.clear_history),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.background,
            )
        }

    }

}

@Composable
fun NotFound() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 104.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.img_not_found),
            contentDescription = "Tracks not found",
        )
        Text(
            text = stringResource(id = R.string.search_screen_placeholder_text_not_found),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )

    }
}

@Composable
fun ConnectionProblem(
    refresh: () -> Unit
) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 104.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.img_no_internet),
            contentDescription = "Tracks not found",
        )

        Text(
            text = stringResource(id = R.string.search_screen_placeholder_text_error),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Button(
            onClick = { refresh() },
            modifier = Modifier
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = stringResource(id = R.string.clear_history),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.background,
            )
        }
    }

}