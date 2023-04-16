package bes.max.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchEditText = findViewById<TextInputEditText>(R.id.search_activity_edit_text)
        val backIcon = findViewById<ImageView>(R.id.search_activity_back_icon)

        backIcon.setOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        searchEditText.addTextChangedListener(textWatcher)
    }
}