package bes.max.playlistmaker.presentation.search

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.ActivitySearchBinding
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.player.PlayerActivity
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val viewModel: SearchViewModel by viewModel()

    private val adapter = TrackListItemAdapter()
    private val adapterForHistory = TrackListItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val onElementClickAction = { track: Track ->
            if (viewModel.clickDebounce()) {
                viewModel.saveTrackToHistory(track)
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(
                    getString(R.string.activity_search_to_activity_player_track_as_json),
                    convertTrackToJson(track)
                )
                startActivity(intent)
            }
        }

        binding.searchActivityRecyclerViewTracks.adapter = adapter
        adapter.onListElementClick = onElementClickAction
        adapterForHistory.listOfTracks = SearchScreenState.History.tracks
        adapterForHistory.onListElementClick = onElementClickAction
        binding.searchActivityHistoryRecyclerView.adapter = adapterForHistory

        binding.searchActivityTextInputLayout.setEndIconOnClickListener {
            binding.searchActivityEditText.text?.clear()
            hideKeyboard()
        }

        binding.searchActivityEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) viewModel.showHistory()
        }

        viewModel.screenState.observe(this) { state ->
            showScreenContent(state)
        }

        binding.searchActivityEditText.setText(viewModel.savedSearchInputText)

        binding.searchActivityBackIcon.setOnClickListener { finish() }


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearchTextChanged(s, binding.searchActivityEditText.hasFocus())
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) {
                    viewModel.showHistory()
                }
            }
        }
        binding.searchActivityEditText.addTextChangedListener(textWatcher)

        binding.searchActivityPlaceholderButton.setOnClickListener {
            if (binding.searchActivityEditText.text?.isNotEmpty() == true) {
                viewModel.searchTrack(binding.searchActivityEditText.text.toString())
            }
        }

        binding.searchActivityHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    override fun onResume() {
        super.onResume()
        if (binding.searchActivityEditText.text?.isNotEmpty() == true) {
            viewModel.searchTrack(binding.searchActivityEditText.text.toString())
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_INPUT_TEXT, viewModel.savedSearchInputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(SEARCH_INPUT_TEXT)
    }

    private fun showScreenContent(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Default -> {
                showDefaultScreenState()
            }

            is SearchScreenState.Loading -> {
                showLoading()
            }

            is SearchScreenState.History -> {
                adapterForHistory.listOfTracks = state.tracks
                showHistory()
            }

            is SearchScreenState.Tracks -> {
                adapter.listOfTracks = state.tracks
                showTracks()
            }

            is SearchScreenState.TracksNotFound -> {
                showNotFound()
            }

            is SearchScreenState.SearchError -> {
                showError()
            }
        }
    }

    private fun showDefaultScreenState() {
        with(binding) {
            searchActivityPlaceholder.visibility = View.GONE
            searchActivityPlaceholderButton.visibility = View.GONE
            searchActivityProgressBar.visibility = View.GONE
            searchActivityRecyclerViewTracks.visibility = View.GONE
            searchActivityHistoryGroup.visibility = View.GONE
        }
    }

    private fun showLoading() {
        with(binding) {
            searchActivityPlaceholder.visibility = View.GONE
            searchActivityProgressBar.visibility = View.VISIBLE
            searchActivityRecyclerViewTracks.visibility = View.GONE
            searchActivityHistoryGroup.visibility = View.GONE
        }
    }

    private fun showHistory() {
        with(binding) {
            searchActivityHistoryGroup.visibility =
                if (adapterForHistory.listOfTracks.isNotEmpty()) View.VISIBLE
                else View.GONE
            searchActivityRecyclerViewTracks.visibility = View.GONE
            searchActivityProgressBar.visibility = View.GONE
        }
    }

    private fun showTracks() {
        with(binding) {
            searchActivityRecyclerViewTracks.visibility = View.VISIBLE
            searchActivityHistoryGroup.visibility = View.GONE
            searchActivityProgressBar.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        with(binding) {
            searchActivityPlaceholder.visibility = View.VISIBLE
            searchActivityPlaceholderImage.setImageResource(R.drawable.img_not_found)
            searchActivityPlaceholderText.setText(getString(R.string.search_activity_placeholder_text_not_found))
            searchActivityPlaceholderButton.visibility = View.GONE
            searchActivityProgressBar.visibility = View.GONE
            searchActivityRecyclerViewTracks.visibility = View.GONE
            searchActivityHistoryGroup.visibility = View.GONE
        }
    }

    private fun showError() {
        with(binding) {
            searchActivityPlaceholder.visibility = View.VISIBLE
            searchActivityPlaceholderImage.setImageResource(R.drawable.img_no_internet)
            searchActivityPlaceholderText.setText(getString(R.string.search_activity_placeholder_text_error))
            searchActivityPlaceholderButton.visibility = View.VISIBLE
            searchActivityProgressBar.visibility = View.GONE
            searchActivityRecyclerViewTracks.visibility = View.GONE
            searchActivityHistoryGroup.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    private fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

    companion object {
        private const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
    }

}