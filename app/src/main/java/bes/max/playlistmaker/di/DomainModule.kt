package bes.max.playlistmaker.di

import bes.max.playlistmaker.domain.mediateka.favorite.FavoriteTracksInteractor
import bes.max.playlistmaker.domain.mediateka.favorite.FavoriteTracksInteractorImpl
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistInteractorImpl
import bes.max.playlistmaker.domain.mediateka.playlistdetails.SharePlaylistUseCase
import bes.max.playlistmaker.domain.search.SearchHistoryInteractor
import bes.max.playlistmaker.domain.search.SearchHistoryInteractorImpl
import bes.max.playlistmaker.domain.search.SearchInNetworkUseCase
import bes.max.playlistmaker.domain.settings.SettingsInteractor
import bes.max.playlistmaker.domain.settings.SettingsInteractorImpl
import bes.max.playlistmaker.domain.settings.SharingInteractor
import bes.max.playlistmaker.domain.settings.SharingInteractorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::SearchHistoryInteractorImpl) bind SearchHistoryInteractor::class

    factoryOf(::SearchInNetworkUseCase)

    factoryOf(::SettingsInteractorImpl) bind SettingsInteractor::class

    factoryOf(::SharingInteractorImpl) bind SharingInteractor::class

    factoryOf(::FavoriteTracksInteractorImpl) bind FavoriteTracksInteractor::class

    factoryOf(::PlaylistInteractorImpl) bind PlaylistInteractor::class

    factoryOf(::SharePlaylistUseCase)

}