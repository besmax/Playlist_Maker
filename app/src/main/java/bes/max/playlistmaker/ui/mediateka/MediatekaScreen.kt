package bes.max.playlistmaker.ui.mediateka

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.ui.common.Title
import bes.max.playlistmaker.ui.mediateka.favorite.FavoriteTracksScreen
import bes.max.playlistmaker.ui.mediateka.playlists.PlaylistsScreen
import kotlinx.coroutines.launch

private const val PAGE_COUNT = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediatekaScreen(
    navController: NavController,
) {

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })

    val selectedTabIndex = remember { pagerState.currentPage }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        
        Title(text = stringResource(R.string.mediateka))

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                selectedContentColor = MaterialTheme.colorScheme.onBackground,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text(text = stringResource(id = R.string.mediateka_screen_tab_title_my)) },
            )

            Tab(
                selected = selectedTabIndex == 1,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.outline,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text(text = stringResource(id = R.string.mediateka_screen_tab_title_favorite_tracks)) },
            )
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