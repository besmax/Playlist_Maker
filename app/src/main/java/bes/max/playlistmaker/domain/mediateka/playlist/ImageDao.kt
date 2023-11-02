package bes.max.playlistmaker.domain.mediateka.playlist

import android.net.Uri

interface ImageDao {

    suspend fun saveImage(uri: Uri) : Uri
}