package bes.max.playlistmaker.presentation.search

sealed interface SearchStatus {
    object SearchNotStarted: SearchStatus
    object SearchLoading: SearchStatus
    object SearchDone: SearchStatus
    object SearchNotFound: SearchStatus
    object SearchError: SearchStatus
}