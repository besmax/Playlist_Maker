package bes.max.playlistmaker

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import bes.max.playlistmaker.data.DatabaseMock
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SearchActivity : AppCompatActivity() {

    private var savedSearchInputText = ""

    companion object {
        const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView = findViewById<RecyclerView>(R.id.search_activity_recycler_view_tracks)
        recyclerView.adapter = TrackListItemAdapter(DatabaseMock.tracks)

        val searchInputLayout =
            findViewById<TextInputLayout>(R.id.search_activity_text_input_layout)
        val searchEditText = findViewById<TextInputEditText>(R.id.search_activity_edit_text)
        val backIcon = findViewById<ImageView>(R.id.search_activity_back_icon)

        searchInputLayout.setEndIconOnClickListener {
            searchEditText.text?.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }

        searchEditText.setText(savedSearchInputText)

        backIcon.setOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                savedSearchInputText = s.toString()
            }
        }
        searchEditText.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_INPUT_TEXT, savedSearchInputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(SEARCH_INPUT_TEXT)
    }
}