package bes.max.playlistmaker.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.search.SearchHistoryInteractor
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase
import bes.max.playlistmaker.presentation.utils.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

class SearchViewModel(
    private val searchInNetworkUseCase: SearchInNetworkUseCase,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _screenState: MutableLiveData<SearchScreenState> =
        MutableLiveData(SearchScreenState.Default)
    val screenState: LiveData<SearchScreenState> = _screenState

    private var searchJob: Job? = null


    private fun searchDebounce(searchText: String) {
        val debounceSearch = debounce<String>(
            delayMillis = SEARCH_DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
            useLastParam = true,
            action = { searchQuery ->
                searchTrack(searchQuery)
            }
        )
        searchJob = viewModelScope.launch {
            debounceSearch.invoke(searchText)
        }
    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

    fun searchTrack(searchRequest: String) {
        _screenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            try {
                val tracks = searchInNetworkUseCase.execute(searchRequest)
                if (tracks.isNullOrEmpty()) {
                    _screenState.postValue(SearchScreenState.TracksNotFound)
                    Log.w(TAG, "List from remote is empty in fun searchTrack")
                } else {
                    SearchScreenState.Tracks.tracks = tracks
                    _screenState.postValue(SearchScreenState.Tracks)
                }
            } catch (e: Exception) {
                _screenState.postValue(SearchScreenState.SearchError)
                Log.e(TAG, "Error Exception in fun searchTrack: ${e.toString()}")
            }
        }
    }

    fun saveTrackToHistory(track: Track) {
        searchHistoryInteractor.saveTrackToHistory(track)
        SearchScreenState.History.tracks = searchHistoryInteractor.getTracksFromHistory()
        if (_screenState.value == SearchScreenState.History) {
            _screenState.value = SearchScreenState.History
        }
    }

    fun showHistory() {
        SearchScreenState.History.tracks = searchHistoryInteractor.getTracksFromHistory()
        _screenState.value = SearchScreenState.History
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
        _screenState.value = SearchScreenState.Default
        SearchScreenState.History.tracks = emptyList()
    }

    fun onSearchTextChanged(searchText: CharSequence?, isFocused: Boolean) {
        if (searchText.isNullOrBlank() && isFocused) {
            cancelSearch()
            SearchScreenState.History.tracks = searchHistoryInteractor.getTracksFromHistory()
            _screenState.value = SearchScreenState.History
        } else {
            searchDebounce(searchText.toString())
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
