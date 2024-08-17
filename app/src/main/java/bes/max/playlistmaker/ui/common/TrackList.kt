package bes.max.playlistmaker.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.ui.theme.YpGray
import bes.max.playlistmaker.ui.theme.ysDisplayFamily
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackListItem(
    track: Track,
    onItemClick: (Track) -> Unit,
    modifier: Modifier = Modifier,
    onLongItemClick: ((Long) -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
            .combinedClickable(
                onClick = { onItemClick(track) },
                onLongClick = {
                    if (onLongItemClick != null) {
                        onLongItemClick(track.trackId)
                    }
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        AsyncImage(
            model = track.bigCover,
            contentDescription = track.trackName,
            error = painterResource(id = R.drawable.ic_picture_not_found),
            placeholder = painterResource(id = R.drawable.ic_picture_not_found),
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)
                .clip(RoundedCornerShape(2.dp)),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = track.trackName,
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,


                )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = track.artistName,
                    fontFamily = ysDisplayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = YpGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 250.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_circle_between_text),
                    contentDescription = "text separator",
                    tint = YpGray,
                    modifier = Modifier.padding(horizontal = 5.dp)


                )
                Text(
                    text = track.trackTime,
                    fontFamily = ysDisplayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = YpGray,
                    modifier = Modifier
                        .weight(1f)
                )
            }

        }
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "right arrow",
            tint = YpGray,
        )
    }
}

@Composable
fun TrackList(
    tracks: List<Track>,
    onItemClick: (Track) -> Unit,
    isReverse: Boolean,
    modifier: Modifier = Modifier,
    onLongItemClick: ((Long) -> Unit)? = null
) {
    LazyColumn(
        reverseLayout = isReverse,
        modifier = modifier.padding(top = 16.dp)
    ) {
        items(
            items = tracks,
            key = { track -> track.trackId }
        ) { track ->
            TrackListItem(
                track = track,
                onItemClick = onItemClick,
                onLongItemClick = onLongItemClick
            )
        }
    }
}
