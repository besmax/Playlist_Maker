package bes.max.playlistmaker.data.mediateka

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import bes.max.playlistmaker.domain.mediateka.playlist.ImageDao
import kotlinx.coroutines.flow.firstOrNull
import java.io.File
import java.io.FileOutputStream

class ImageDaoImpl(
    private val preferencesDataStore: DataStore<Preferences>,
    private val context: Context
) : ImageDao {

    override suspend fun saveImage(uri: Uri): Uri {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), folderName)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        var number = getNumberForCover()
        number++
        setNumberForCover(number)
        val file = File(filePath, "cover_$number.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        Log.d(TAG, "file saved with uri: ${file.toUri()}")
        return file.toUri()
    }

    private suspend fun getNumberForCover() : Int {
        val preferences = preferencesDataStore.data.firstOrNull()
        return preferences?.get(IMAGE_PREFERENCES_KEY) ?: 0
    }


    private suspend fun setNumberForCover(number: Int) {
        preferencesDataStore.edit { preferences ->
            preferences[IMAGE_PREFERENCES_KEY] = number
        }
    }

    companion object {
        private const val TAG = "ImageDaoImpl"
        private val IMAGE_PREFERENCES_KEY = intPreferencesKey("image_preference_key")
        private const val folderName = "playlistcovers"
    }
}