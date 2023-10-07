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
import bes.max.playlistmaker.domain.mediateka.FavoriteTracksInteractor
import bes.max.playlistmaker.domain.mediateka.FavoriteTracksInteractorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::PlayerInteractorImpl) bind PlayerInteractor::class

    factoryOf(::SearchHistoryInteractorImpl) bind SearchHistoryInteractor::class

    factoryOf(::SearchInNetworkUseCase)

    factoryOf(::SettingsInteractorImpl) bind SettingsInteractor::class

    factoryOf(::SharingInteractorImpl) bind SharingInteractor::class

    factoryOf(::FavoriteTracksInteractorImpl) bind FavoriteTracksInteractor::class

}