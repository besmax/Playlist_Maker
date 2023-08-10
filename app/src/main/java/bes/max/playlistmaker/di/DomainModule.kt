package bes.max.playlistmaker.di

import bes.max.playlistmaker.domain.player.PlayerInteractor
import bes.max.playlistmaker.domain.player.PlayerInteractorImpl
import bes.max.playlistmaker.domain.search.SearchHistoryInteractor
import bes.max.playlistmaker.domain.search.SearchHistoryInteractorImpl
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase
import bes.max.playlistmaker.domain.settings.SettingsInteractor
import bes.max.playlistmaker.domain.settings.SettingsInteractorImpl
import bes.max.playlistmaker.domain.settings.SharingInteractor
import bes.max.playlistmaker.domain.settings.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module {

    factory<PlayerInteractor> {
        PlayerInteractorImpl(player = get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(trackRepository = get())
    }

    factory {
        SearchInNetworkUseCase(tracksRepository = get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }

}