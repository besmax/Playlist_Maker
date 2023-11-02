package bes.max.playlistmaker.di

import bes.max.playlistmaker.presentation.mediateka.MediatekaViewModel
import bes.max.playlistmaker.presentation.mediateka.favorite.FavoriteTracksViewModel
import bes.max.playlistmaker.presentation.mediateka.newplaylist.NewPlaylistViewModel
import bes.max.playlistmaker.presentation.mediateka.playlistdetails.PlaylistDetailsViewModel
import bes.max.playlistmaker.presentation.mediateka.playlists.PlaylistsViewModel
import bes.max.playlistmaker.presentation.player.PlayerViewModel
import bes.max.playlistmaker.presentation.search.SearchViewModel
import bes.max.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::PlayerViewModel)

    viewModelOf(::SearchViewModel)

    viewModelOf(::SettingsViewModel)

    viewModelOf(::FavoriteTracksViewModel)

    viewModelOf(::PlaylistsViewModel)

    viewModelOf(::MediatekaViewModel)

    viewModelOf(::NewPlaylistViewModel)

    viewModelOf(::PlaylistDetailsViewModel)

}