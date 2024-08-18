package bes.max.playlistmaker.ui.mediateka

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.ui.common.Title
import bes.max.playlistmaker.ui.mediateka.favorite.FavoriteTracksScreen
import bes.max.playlistmaker.ui.mediateka.playlists.PlaylistsScreen
import bes.max.playlistmaker.ui.theme.YpBlack
import bes.max.playlistmaker.ui.theme.ysDisplayFamily
import kotlinx.coroutines.launch

private const val PAGE_COUNT = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediatekaScreen(
    navController: NavController,
) {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })

    var selectedTabIndex = pagerState.currentPage

    val tabs = listOf(
        stringResource(id = R.string.mediateka_screen_tab_title_playlists),
        stringResource(id = R.string.mediateka_screen_tab_title_favorite_tracks)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Title(text = stringResource(R.string.mediateka))

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.background,
            indicator = {
                Box(
                    Modifier
                        .tabIndicatorOffset(it[selectedTabIndex])
                        .height(2.dp)
                        .border(2.dp, MaterialTheme.colorScheme.onBackground)
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {

            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        scope.launch {
                            selectedTabIndex = index
                            pagerState.animateScrollToPage(index)
                            Log.e("AAAAA", "selectedTabIndex=${selectedTabIndex}")
                        }
                    },
                ) {
                    Text(
                        text = title,
                        fontFamily = ysDisplayFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> FavoriteTracksScreen(navController = navController)
                1 -> PlaylistsScreen(navController = navController)
            }
        }

    }
}