package bes.max.playlistmaker.ui

sealed interface SearchStatus {
    object SearchNotStarted: SearchStatus
    object SearchLoading: SearchStatus
    object SearchDone: SearchStatus
    object SearchNotFound: SearchStatus
    object SearchError: SearchStatus
}