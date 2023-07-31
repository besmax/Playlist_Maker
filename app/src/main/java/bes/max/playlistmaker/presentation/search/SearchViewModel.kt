package bes.max.playlistmaker.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.search.SearchHistoryInteractor
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

class SearchViewModel(
    private val searchInNetworkUseCase: SearchInNetworkUseCase,
    private val searchHistoryInteractor: SearchHistoryInteractor
    ) : ViewModel() {

    private val _searchStatus: MutableLiveData<SearchStatus> = MutableLiveData(SearchStatus.SearchNotStarted)
    val searchStatus: LiveData<SearchStatus> = _searchStatus!!

    private val _tracks: MutableLiveData<List<Track>> = MutableLiveData(emptyList())
    val tracks: LiveData<List<Track>> = _tracks!!

    private val _historyTracks: MutableLiveData<List<Track>> = MutableLiveData(emptyList())
    val historyTracks: LiveData<List<Track>> = _historyTracks!!

    fun searchTrack(searchRequest: String) {
        _searchStatus.value = SearchStatus.SearchLoading
        viewModelScope.launch {
            try {
                _tracks.value = searchInNetworkUseCase.execute(searchRequest)
                if (tracks.value.isNullOrEmpty()) {
                    _searchStatus.value = SearchStatus.SearchNotFound
                    Log.w(TAG, "List from remote is empty in fun searchTrack")
                } else {
                    _searchStatus.value = SearchStatus.SearchDone
                }
            } catch (e: Exception) {
                _searchStatus.value = SearchStatus.SearchError
                Log.e(TAG, "Error Exception in fun searchTrack: ${e.toString()}")
            }
        }
    }

    fun clearTracks() {
        _tracks.value = emptyList()
    }

    fun getTracksFromHistory() {
        _historyTracks.value = searchHistoryInteractor.getTracksFromHistory()
    }

    fun saveTrackToHistory(track: Track) {
        searchHistoryInteractor.saveTrackToHistory(track)
        getTracksFromHistory()
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        _historyTracks.value = emptyList()
    }
}