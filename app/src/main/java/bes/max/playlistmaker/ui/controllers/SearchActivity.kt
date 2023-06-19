package bes.max.playlistmaker.ui.controllers

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.R
import bes.max.playlistmaker.ui.TrackListItemAdapter
import bes.max.playlistmaker.databinding.ActivitySearchBinding
import bes.max.playlistmaker.model.ITunesSearchApiResponse
import bes.max.playlistmaker.model.Track
import bes.max.playlistmaker.network.ITunesSearchApi
import bes.max.playlistmaker.network.SearchApiStatus
import bes.max.playlistmaker.ui.SearchHistory
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var savedSearchInputText = ""

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private var tracks = mutableListOf<Track>()
    private val adapter = TrackListItemAdapter()
    private val adapterForHistory = TrackListItemAdapter()
    private val sharedPreferences by lazy {
        getSharedPreferences(getString(R.string.search_history_preferences), MODE_PRIVATE)
    }
    private val searchHistory by lazy {
        SearchHistory(
            sharedPreferences,
            getString(R.string.search_history_preferences_key)
        )
    }

    companion object {
        const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val onElementClickAction = { track: Track ->
            saveTrackInHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(
                getString(R.string.activity_search_to_activity_player_track_as_json),
                convertTrackToJson(track)
            )
            startActivity(intent)
        }

        binding.searchActivityRecyclerViewTracks.adapter = adapter
        adapter.onListElementClick = onElementClickAction
        adapterForHistory.listOfTracks = searchHistory.history
        adapterForHistory.onListElementClick = onElementClickAction
        binding.searchActivityHistoryRecyclerView.adapter = adapterForHistory

        binding.searchActivityTextInputLayout.setEndIconOnClickListener {
            binding.searchActivityEditText.text?.clear()
            hideKeyboard()
            tracks.clear()
            adapter.notifyDataSetChanged()
            binding.searchActivityPlaceholder.visibility = View.GONE
        }

        binding.searchActivityEditText.setOnFocusChangeListener { _, hasFocus ->
            showOrHideHistory(hasFocus)
        }

        binding.searchActivityEditText.setText(savedSearchInputText)

        binding.searchActivityBackIcon.setOnClickListener { finish() }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.searchActivityEditText.hasFocus() && s.isNullOrEmpty()) {
                    showOrHideHistory(binding.searchActivityEditText.hasFocus())
                    tracks.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                savedSearchInputText = s.toString()
            }
        }
        binding.searchActivityEditText.addTextChangedListener(textWatcher)

        binding.searchActivityEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchActivityEditText.text?.isNotEmpty() == true) {
                    getTrack(binding.searchActivityEditText.text.toString())
                    adapter.listOfTracks = tracks
                    adapter.notifyDataSetChanged()
                    showOrHideHistory()
                }
                true
            }
            false
        }

        binding.searchActivityPlaceholderButton.setOnClickListener {
            if (binding.searchActivityEditText.text?.isNotEmpty() == true) {
                getTrack(binding.searchActivityEditText.text.toString())
                adapter.listOfTracks = tracks
                adapter.notifyDataSetChanged()
            }
        }

        binding.searchActivityHistoryButton.setOnClickListener {
            searchHistory.clearTracksHistory()
            adapterForHistory.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        if (binding.searchActivityEditText.text?.isNotEmpty() == true) {
            getTrack(binding.searchActivityEditText.text.toString())
            adapter.listOfTracks = tracks
            adapter.notifyDataSetChanged()
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

    private fun getTrack(query: String) {
        tracks.clear()
        adapter.notifyDataSetChanged()
        ITunesSearchApi.iTunesSearchApiService.search(query).enqueue(object :
            Callback<ITunesSearchApiResponse> {
            override fun onResponse(
                call: Call<ITunesSearchApiResponse>,
                response: Response<ITunesSearchApiResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()!!.results)
                            adapter.notifyDataSetChanged()
                            showPlaceHolder(SearchApiStatus.DONE)
                        }
                        if (tracks.isEmpty()) {
                            showPlaceHolder(SearchApiStatus.NOT_FOUND)
                        }
                    }
                    else -> showPlaceHolder(SearchApiStatus.ERROR)
                }
            }

            override fun onFailure(call: Call<ITunesSearchApiResponse>, t: Throwable) {
                showPlaceHolder(SearchApiStatus.ERROR)
            }
        })
    }

    private fun showPlaceHolder(status: SearchApiStatus) {
        when (status) {
            SearchApiStatus.ERROR -> {
                binding.searchActivityPlaceholder.visibility = View.VISIBLE
                binding.searchActivityPlaceholderImage.setImageResource(R.drawable.img_no_internet)
                binding.searchActivityPlaceholderText.setText(getString(R.string.search_activity_placeholder_text_error))
                binding.searchActivityPlaceholderButton.visibility = View.VISIBLE
            }

            SearchApiStatus.NOT_FOUND -> {
                binding.searchActivityPlaceholder.visibility = View.VISIBLE
                binding.searchActivityPlaceholderImage.setImageResource(R.drawable.img_not_found)
                binding.searchActivityPlaceholderText.setText(getString(R.string.search_activity_placeholder_text_not_found))
                binding.searchActivityPlaceholderButton.visibility = View.GONE
            }

            else -> binding.searchActivityPlaceholder.visibility = View.GONE
        }
    }

    private fun showOrHideHistory(hasFocus: Boolean = false) {
        searchHistory.getHistoryTracks()
        adapterForHistory.listOfTracks = searchHistory.history
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

    private fun saveTrackInHistory(track: Track) {
        searchHistory.saveTrack(track)
        adapterForHistory.listOfTracks = searchHistory.history
        adapterForHistory.notifyDataSetChanged()
    }

}