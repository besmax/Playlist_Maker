package bes.max.playlistmaker.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import bes.max.playlistmaker.di.Creator

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val creator = Creator(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(
                creator.getSearchInNetworkUseCase(),
                creator.getSearchHistoryInteractor()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}