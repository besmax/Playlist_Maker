package bes.max.playlistmaker.di

import android.content.Context
import bes.max.playlistmaker.data.dao.SearchHistoryDaoImpl
import bes.max.playlistmaker.data.network.RetrofitNetworkClient
import bes.max.playlistmaker.data.player.PlayerImpl
import bes.max.playlistmaker.data.repositories.TracksRepositoryImpl
import bes.max.playlistmaker.domain.api.TracksRepository
import bes.max.playlistmaker.domain.search.ClearHistoryUseCase
import bes.max.playlistmaker.domain.search.GetTracksFromHistoryUseCase
import bes.max.playlistmaker.domain.search.SaveTrackInHistoryUseCase
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase

class Creator(private val context: Context?) {

    private var tracksRepository: TracksRepository? = null

    init {
        if (context != null) {
            val networkClient = RetrofitNetworkClient()
            val searchHistoryDao = SearchHistoryDaoImpl(context)
            tracksRepository = TracksRepositoryImpl(networkClient, searchHistoryDao)
        }
    }

    fun getNewPlayerImpl()  = PlayerImpl()

    fun getTracksRepository(): TracksRepository? = tracksRepository

    fun getSearchInNetworkUseCase(): SearchInNetworkUseCase {
        lateinit var searchInNetworkUseCase: SearchInNetworkUseCase
        if (tracksRepository != null) {
            searchInNetworkUseCase = SearchInNetworkUseCase(tracksRepository!!)
        }
        return searchInNetworkUseCase
    }

    fun getSaveTrackInHistoryUseCase(): SaveTrackInHistoryUseCase {
        lateinit var saveTrackInHistoryUseCase: SaveTrackInHistoryUseCase
        if (tracksRepository != null) {
            saveTrackInHistoryUseCase = SaveTrackInHistoryUseCase(tracksRepository!!)
        }
        return saveTrackInHistoryUseCase
    }

    fun getGetTracksFromHistoryUseCase(): GetTracksFromHistoryUseCase {
        lateinit var getTracksFromHistoryUseCase: GetTracksFromHistoryUseCase
        if (tracksRepository != null) {
            getTracksFromHistoryUseCase = GetTracksFromHistoryUseCase(tracksRepository!!)
        }
        return getTracksFromHistoryUseCase
    }

    fun getClearHistoryUseCase(): ClearHistoryUseCase {
        lateinit var clearHistoryUseCase: ClearHistoryUseCase
        if (tracksRepository != null) {
            clearHistoryUseCase = ClearHistoryUseCase(tracksRepository!!)
        }
        return clearHistoryUseCase
    }

}