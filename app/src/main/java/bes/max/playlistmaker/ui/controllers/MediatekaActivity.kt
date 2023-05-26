package bes.max.playlistmaker.ui.controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.databinding.ActivityMediatekaBinding

class MediatekaActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityMediatekaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}