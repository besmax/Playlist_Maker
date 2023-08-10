package bes.max.playlistmaker.presentation.search

import android.os.Handler
import android.os.Looper
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

    private val _screenState: MutableLiveData<SearchScreenState> =
        MutableLiveData(SearchScreenState.Default)
    val screenState: LiveData<SearchScreenState> = _screenState

    var savedSearchInputText = ""

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTrack(savedSearchInputText) }

    private fun searchDebounce(searchText: String) {
        savedSearchInputText = searchText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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
        _screenState.value = SearchScreenState.History
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
            SearchScreenState.History.tracks = searchHistoryInteractor.getTracksFromHistory()
            _screenState.value = SearchScreenState.History
        } else {
            searchDebounce(searchText.toString())
        }
    }

    fun clickDebounce(): Boolean {
        val currentFlag = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return currentFlag
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
