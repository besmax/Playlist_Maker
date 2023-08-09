package bes.max.playlistmaker.di

import android.content.Context
import bes.max.playlistmaker.data.mappers.TrackDtoMapper
import bes.max.playlistmaker.data.search.SearchHistoryDaoImpl
import bes.max.playlistmaker.data.settings.SettingsDaoImpl
import bes.max.playlistmaker.data.network.RetrofitNetworkClient
import bes.max.playlistmaker.data.player.PlayerImpl
import bes.max.playlistmaker.data.settings.SettingsRepositoryImpl
import bes.max.playlistmaker.data.search.TracksRepositoryImpl
import bes.max.playlistmaker.data.settings.ExternalNavigatorImpl
import bes.max.playlistmaker.domain.search.TracksRepository
import bes.max.playlistmaker.domain.search.SearchHistoryInteractorImpl
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase
import bes.max.playlistmaker.domain.settings.ExternalNavigator
import bes.max.playlistmaker.domain.settings.SettingsInteractor
import bes.max.playlistmaker.domain.settings.SettingsInteractorImpl
import bes.max.playlistmaker.domain.settings.SharingInteractor
import bes.max.playlistmaker.domain.settings.SharingInteractorImpl

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

    fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun getSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }

}