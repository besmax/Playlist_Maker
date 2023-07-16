package bes.max.playlistmaker.ui.controllers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import bes.max.playlistmaker.ui.SearchStatus
import bes.max.playlistmaker.ui.SearchViewModel
import bes.max.playlistmaker.ui.SearchViewModelFactory
import bes.max.playlistmaker.ui.TrackListItemAdapter
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {

    private var savedSearchInputText = ""

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(context = this)
    }

    private val adapter = TrackListItemAdapter()
    private val adapterForHistory = TrackListItemAdapter()

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { viewModel.searchTrack(savedSearchInputText) }

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val onElementClickAction = { track: Track ->
            if (clickDebounce()) {
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
        adapterForHistory.listOfTracks = viewModel.historyTracks.value ?: emptyList()
        adapterForHistory.onListElementClick = onElementClickAction
        binding.searchActivityHistoryRecyclerView.adapter = adapterForHistory

        binding.searchActivityTextInputLayout.setEndIconOnClickListener {
            binding.searchActivityEditText.text?.clear()
            hideKeyboard()
            viewModel.clearTracks()
            binding.searchActivityPlaceholder.visibility = View.GONE
        }

        binding.searchActivityEditText.setOnFocusChangeListener { _, hasFocus ->
            showOrHideHistory(hasFocus)
        }

        binding.searchActivityEditText.setText(savedSearchInputText)

        binding.searchActivityBackIcon.setOnClickListener { finish() }

        viewModel.tracks.observe(this) {
            if (!it.isNullOrEmpty()) adapter.listOfTracks = it
        }

        viewModel.searchStatus.observe(this) {
            showPlaceHolder()
        }

        viewModel.historyTracks.observe(this) {
            adapterForHistory.listOfTracks = viewModel.historyTracks.value ?: emptyList()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.searchActivityEditText.hasFocus() && s.isNullOrBlank()) {
                    showOrHideHistory(binding.searchActivityEditText.hasFocus())
                    viewModel.clearTracks()
                } else {
                    searchDebounce(s)
                    showOrHideHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.searchActivityEditText.hasFocus() && s.isNullOrBlank()) {
                    showOrHideHistory(binding.searchActivityEditText.hasFocus())
                    viewModel.clearTracks()
                    adapter.listOfTracks = viewModel.tracks.value!!
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
            adapter.listOfTracks = viewModel.tracks.value ?: emptyList()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_INPUT_TEXT, savedSearchInputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(SEARCH_INPUT_TEXT)
    }

    private fun showPlaceHolder() {
        when (viewModel.searchStatus.value) {
            SearchStatus.SearchError -> {
                showError()
            }

            SearchStatus.SearchNotFound -> {
                showNotFound()
            }
            SearchStatus.SearchLoading -> {
                binding.searchActivityPlaceholder.visibility = View.GONE
                binding.searchActivityProgressBar.visibility = View.VISIBLE
            }

            SearchStatus.SearchNotStarted -> {
                binding.searchActivityPlaceholder.visibility = View.GONE
                binding.searchActivityPlaceholderButton.visibility = View.GONE
                binding.searchActivityProgressBar.visibility = View.GONE
            }

            else -> {
                binding.searchActivityPlaceholder.visibility = View.GONE
                binding.searchActivityProgressBar.visibility = View.GONE
            }
        }
    }

    private fun showNotFound() {
        with(binding) {
            searchActivityPlaceholder.visibility = View.VISIBLE
            searchActivityPlaceholderImage.setImageResource(R.drawable.img_not_found)
            searchActivityPlaceholderText.setText(getString(R.string.search_activity_placeholder_text_not_found))
            searchActivityPlaceholderButton.visibility = View.GONE
            searchActivityProgressBar.visibility = View.GONE
        }
    }

    private fun showError() {
        with(binding) {
            searchActivityPlaceholder.visibility = View.VISIBLE
            searchActivityPlaceholderImage.setImageResource(R.drawable.img_no_internet)
            searchActivityPlaceholderText.setText(getString(R.string.search_activity_placeholder_text_error))
            searchActivityPlaceholderButton.visibility = View.VISIBLE
            searchActivityProgressBar.visibility = View.GONE
        }
    }

    private fun showOrHideHistory(hasFocus: Boolean = false) {
        viewModel.getTracksFromHistory()
        adapterForHistory.notifyDataSetChanged()
        binding.searchActivityHistoryGroup.visibility =
            if (hasFocus && binding.searchActivityEditText.text.isNullOrEmpty() && adapterForHistory.listOfTracks.isNotEmpty()) View.VISIBLE
            else View.GONE
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

    private fun searchDebounce(s: CharSequence?) {
        if (!s.isNullOrBlank()) {
            savedSearchInputText = s.toString()
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clickDebounce(): Boolean {
        val currentFlag = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return currentFlag
    }

    companion object {
        private const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}