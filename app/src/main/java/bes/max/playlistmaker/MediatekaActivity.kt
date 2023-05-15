package bes.max.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.databinding.ActivityMediatekaBinding

class MediatekaActivity : AppCompatActivity() {

    private var _binding: ActivityMediatekaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}