package bes.max.playlistmaker.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.search.SearchScreenState
import bes.max.playlistmaker.presentation.search.SearchViewModel
import bes.max.playlistmaker.presentation.utils.debounce
import bes.max.playlistmaker.ui.common.Title
import bes.max.playlistmaker.ui.theme.YpBlack
import bes.max.playlistmaker.ui.theme.YpLightGray
import bes.max.playlistmaker.ui.theme.ysDisplayFamily
import com.google.gson.Gson

private const val CLICK_DEBOUNCE_DELAY = 1000L

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {

    val uiScreenState by viewModel.screenState.observeAsState(SearchScreenState.Default)
    val coroutineScope = rememberCoroutineScope()

    val onElementClickAction = debounce<Track>(
        delayMillis = CLICK_DEBOUNCE_DELAY,
        coroutineScope = coroutineScope,
        useLastParam = false,
        action = { track ->
            viewModel.saveTrackToHistory(track)
            val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(Gson().toJson(track))
            navController.navigate(action)
        }
    )

    SearchScreenContent(
        uiScreenState = uiScreenState,
        onInputChange = { input -> viewModel.searchDebounce(input) }
    )

}

@Composable
fun SearchScreenContent(
    uiScreenState: SearchScreenState,
    onInputChange: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Title(text = stringResource(id = R.string.search))
        SearchInput(onInputChange = onInputChange)

        when (uiScreenState) {
            is SearchScreenState.Default -> {}

            is SearchScreenState.Loading -> {}

            is SearchScreenState.History -> {}

            is SearchScreenState.Tracks -> {}

            is SearchScreenState.TracksNotFound -> {}

            is SearchScreenState.SearchError -> {}
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