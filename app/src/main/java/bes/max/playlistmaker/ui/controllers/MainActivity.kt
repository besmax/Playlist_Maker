package bes.max.playlistmaker.ui.controllers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.buttonMediateka.setOnClickListener {
            val intent = Intent(this, MediatekaActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}