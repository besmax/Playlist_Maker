package bes.max.playlistmaker.di

import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.player.PlayerViewModel
import bes.max.playlistmaker.presentation.search.SearchViewModel
import bes.max.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (track: Track) ->
        PlayerViewModel(track = track, playerInteractor = get())
    }

    viewModel {
        SearchViewModel(searchHistoryInteractor = get(), searchInNetworkUseCase = get())
    }

    viewModel {
        SettingsViewModel(settingsInteractor = get(), sharingInteractor = get())
    }

}