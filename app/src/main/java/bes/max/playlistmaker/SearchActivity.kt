package bes.max.playlistmaker

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bes.max.playlistmaker.databinding.ActivitySearchBinding
import bes.max.playlistmaker.model.ITunesSearchApiResponse
import bes.max.playlistmaker.model.Track
import bes.max.playlistmaker.network.ITunesSearchApi
import bes.max.playlistmaker.network.SearchApiStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var savedSearchInputText = ""

    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    private var tracks = mutableListOf<Track>()
    private val adapter = TrackListItemAdapter()

    private var status = SearchApiStatus.DONE

    companion object {
        const val SEARCH_INPUT_TEXT = "SEARCH_INPUT_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchActivityRecyclerViewTracks.adapter = adapter

        binding.searchActivityTextInputLayout.setEndIconOnClickListener {
            binding.searchActivityEditText.text?.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            val view = this.currentFocus
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
            tracks.clear()
            adapter.notifyDataSetChanged()
            binding.searchActivityPlaceholder.visibility = View.GONE
        }

        binding.searchActivityEditText.setText(savedSearchInputText)

        binding.searchActivityBackIcon.setOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

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
                }
                true
            }
            false
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
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()!!.results)
                        adapter.notifyDataSetChanged()
                        status = SearchApiStatus.DONE
                    }
                    if (tracks.isEmpty()) {
                        status = SearchApiStatus.NOT_FOUND
                    }
                } else {
                    status = SearchApiStatus.ERROR
                }
                showPlaceHolder()
            }

            override fun onFailure(call: Call<ITunesSearchApiResponse>, t: Throwable) {
                status = SearchApiStatus.ERROR
                showPlaceHolder()
            }
        })

    }

    private fun showPlaceHolder() {
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

}