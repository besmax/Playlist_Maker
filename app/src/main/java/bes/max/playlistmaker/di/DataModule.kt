package bes.max.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import bes.max.playlistmaker.data.mappers.TrackDtoMapper
import bes.max.playlistmaker.data.network.ITunesSearchApiService
import bes.max.playlistmaker.data.network.NetworkClient
import bes.max.playlistmaker.data.network.RetrofitNetworkClient
import bes.max.playlistmaker.data.search.SearchHistoryDao
import bes.max.playlistmaker.data.search.SearchHistoryDaoImpl
import bes.max.playlistmaker.data.settings.ExternalNavigatorImpl
import bes.max.playlistmaker.data.settings.SettingsDao
import bes.max.playlistmaker.data.settings.SettingsDaoImpl
import bes.max.playlistmaker.domain.settings.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SETTINGS_PREFERENCES = "settings_preferences"
private const val SHARED_PREF_KEY = "search_history_preferences_key"
private const val ITUNES_BASE_URL = "https://itunes.apple.com"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCES
)

val dataModule = module {

    factory {
        Gson()
    }

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    single<NetworkClient> {
        RetrofitNetworkClient(iTunesSearchApiService = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREF_KEY, 0)
    }

    single<SearchHistoryDao> {
        SearchHistoryDaoImpl(sharedPreferences = get(), gson = get())
    }

    factory {
        TrackDtoMapper()
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
    }

    single<SettingsDao> {
        SettingsDaoImpl(context = androidContext(), preferencesDataStore = get())
    }

    single<ITunesSearchApiService> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApiService::class.java)
    }

}