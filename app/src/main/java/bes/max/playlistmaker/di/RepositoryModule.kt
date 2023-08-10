package bes.max.playlistmaker.di

import bes.max.playlistmaker.data.player.PlayerImpl
import bes.max.playlistmaker.data.search.TracksRepositoryImpl
import bes.max.playlistmaker.data.settings.SettingsRepositoryImpl
import bes.max.playlistmaker.domain.player.Player
import bes.max.playlistmaker.domain.search.TracksRepository
import bes.max.playlistmaker.domain.settings.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(settingsDao = get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(
            networkClient = get(),
            searchHistoryDao = get(),
            trackDtoMapper = get()
        )
    }

    factory<Player> {
        PlayerImpl()
    }

}