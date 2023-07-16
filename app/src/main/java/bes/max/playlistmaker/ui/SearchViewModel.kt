package bes.max.playlistmaker.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.playlistmaker.data.NetworkClient
import bes.max.playlistmaker.data.dao.SearchHistoryDaoImpl
import bes.max.playlistmaker.data.network.RetrofitNetworkClient
import bes.max.playlistmaker.data.repositories.TracksRepositoryImpl
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.domain.search.ClearHistoryUseCase
import bes.max.playlistmaker.domain.search.GetTracksFromHistoryUseCase
import bes.max.playlistmaker.domain.search.SaveTrackInHistoryUseCase
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(private val context: Context) : ViewModel() {



    private val _searchStatus: MutableLiveData<SearchStatus> = MutableLiveData(SearchStatus.SearchNotStarted)
    val searchStatus: LiveData<SearchStatus> = _searchStatus!!

    private val _tracks: MutableLiveData<List<Track>> = MutableLiveData(emptyList())
    val tracks: LiveData<List<Track>> = _tracks!!

    private val _historyTracks: MutableLiveData<List<Track>> = MutableLiveData(emptyList())
    val historyTracks: LiveData<List<Track>> = _historyTracks!!


    private val repository = TracksRepositoryImpl(RetrofitNetworkClient(), SearchHistoryDaoImpl(context))
    private lateinit var searchInNetworkUseCase: SearchInNetworkUseCase
    private lateinit var saveTrackInHistoryUseCase: SaveTrackInHistoryUseCase
    private lateinit var getTracksFromHistoryUseCase: GetTracksFromHistoryUseCase
    private lateinit var clearHistoryUseCase: ClearHistoryUseCase

    init {
       searchInNetworkUseCase = SearchInNetworkUseCase(repository)
       saveTrackInHistoryUseCase = SaveTrackInHistoryUseCase(repository)
       getTracksFromHistoryUseCase = GetTracksFromHistoryUseCase(repository)
       clearHistoryUseCase = ClearHistoryUseCase(repository)
    }

    fun searchTrack(searchRequest: String) {
        _searchStatus.value = SearchStatus.SearchLoading
        viewModelScope.launch {
            try {
                _tracks.value = searchInNetworkUseCase.execute(searchRequest)
                if (tracks.value.isNullOrEmpty()) {
                    _searchStatus.value = SearchStatus.SearchNotFound
                    Log.w("SearchViewModel", "List from is empty in fun searchTrack")
                } else {
                    _searchStatus.value = SearchStatus.SearchDone
                }
            } catch (e: Exception) {
                _searchStatus.value = SearchStatus.SearchError
                Log.w("SearchViewModel", "Error Exception in fun searchTrack: ${e.toString()}")
            }
        }
    }

    fun clearTracks() {
        _tracks.value = emptyList()
    }

    fun getTracksFromHistory() {
        _historyTracks.value = getTracksFromHistoryUseCase.execute()
    }

    fun saveTrackToHistory(track: Track) {
        saveTrackInHistoryUseCase.execute(track)
        getTracksFromHistory()
    }

    fun clearHistory() {
        clearHistoryUseCase.execute()
        _historyTracks.value = emptyList()
    }
}