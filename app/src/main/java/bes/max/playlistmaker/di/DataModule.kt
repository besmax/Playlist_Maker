package bes.max.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
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
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    factoryOf(::Gson)

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    singleOf(::RetrofitNetworkClient) bind NetworkClient::class

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREF_KEY, 0)
    }

    singleOf(::SearchHistoryDaoImpl) bind SearchHistoryDao::class

    factoryOf(::TrackDtoMapper)

    factoryOf(::ExternalNavigatorImpl) bind ExternalNavigator::class

    singleOf(::SettingsDaoImpl) bind SettingsDao::class

    single<ITunesSearchApiService> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApiService::class.java)
    }

    factory {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

}