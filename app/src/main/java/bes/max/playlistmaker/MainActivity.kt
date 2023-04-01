package bes.max.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.button_search)
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val mediatekaButton = findViewById<Button>(R.id.button_mediateka)
        mediatekaButton.setOnClickListener {
            val intent = Intent(this, MediatekaActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<Button>(R.id.button_settings)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}