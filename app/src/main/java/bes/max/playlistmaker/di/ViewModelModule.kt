package bes.max.playlistmaker.di

import bes.max.playlistmaker.presentation.player.PlayerViewModel
import bes.max.playlistmaker.presentation.search.SearchViewModel
import bes.max.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::PlayerViewModel)

    viewModelOf(::SearchViewModel)

    viewModelOf(::SettingsViewModel)

}