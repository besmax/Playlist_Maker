package bes.max.playlistmaker.presentation.mediateka.favorite

import bes.max.playlistmaker.domain.models.Track

sealed interface FavoriteScreenState {

    object Loading : FavoriteScreenState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteScreenState

    object Empty : FavoriteScreenState

}