package bes.max.playlistmaker.di

import android.content.Context
import bes.max.playlistmaker.data.mappers.TrackDtoMapper
import bes.max.playlistmaker.data.dao.SearchHistoryDaoImpl
import bes.max.playlistmaker.data.dao.SettingsDaoImpl
import bes.max.playlistmaker.data.network.RetrofitNetworkClient
import bes.max.playlistmaker.data.repositories.PlayerImpl
import bes.max.playlistmaker.data.repositories.SettingsRepositoryImpl
import bes.max.playlistmaker.data.repositories.TracksRepositoryImpl
import bes.max.playlistmaker.domain.api.TracksRepository
import bes.max.playlistmaker.domain.search.SearchHistoryInteractorImpl
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase
import bes.max.playlistmaker.domain.settings.SettingsInteractor
import bes.max.playlistmaker.domain.settings.SettingsInteractorImpl

class Creator(private val context: Context) {

    private val tracksRepository: TracksRepository

    init {
        val networkClient = RetrofitNetworkClient()
        val searchHistoryDao = SearchHistoryDaoImpl(context)
        tracksRepository = TracksRepositoryImpl(networkClient, searchHistoryDao, TrackDtoMapper())
    }

    fun getNewPlayerImpl() = PlayerImpl()

    fun getSearchInNetworkUseCase() = SearchInNetworkUseCase(tracksRepository)

    fun getSearchHistoryInteractor() = SearchHistoryInteractorImpl(tracksRepository)

    fun getSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            SettingsRepositoryImpl(
                SettingsDaoImpl(context)
            )
        )
    }


}