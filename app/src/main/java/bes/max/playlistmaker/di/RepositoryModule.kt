package bes.max.playlistmaker.di

import bes.max.playlistmaker.data.mediateka.FavoriteTracksRepositoryImpl
import bes.max.playlistmaker.data.mediateka.PlaylistRepositoryImpl
import bes.max.playlistmaker.data.player.PlayerImpl
import bes.max.playlistmaker.data.player.PlayerServiceImpl
import bes.max.playlistmaker.data.search.TracksRepositoryImpl
import bes.max.playlistmaker.data.settings.SettingsRepositoryImpl
import bes.max.playlistmaker.domain.mediateka.favorite.FavoriteTracksRepository
import bes.max.playlistmaker.domain.mediateka.playlist.PlaylistRepository
import bes.max.playlistmaker.domain.player.Player
import bes.max.playlistmaker.domain.search.TracksRepository
import bes.max.playlistmaker.domain.settings.SettingsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    singleOf(::TracksRepositoryImpl) bind TracksRepository::class

    singleOf(::PlayerServiceImpl) bind Player::class

    singleOf(::FavoriteTracksRepositoryImpl) bind FavoriteTracksRepository::class

    singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class

}