package bes.max.playlistmaker.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentSearchBinding
import bes.max.playlistmaker.domain.models.Track
import bes.max.playlistmaker.presentation.utils.BindingFragment
import bes.max.playlistmaker.presentation.utils.debounce
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val searchViewModel: SearchViewModel by viewModel()
    private val adapter = TrackListItemAdapter()
    private val adapterForHistory = TrackListItemAdapter()
    private var textWatcher: TextWatcher? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onElementClickAction = debounce<Track>(
            delayMillis = CLICK_DEBOUNCE_DELAY,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = false,
            action = { track ->
                searchViewModel.saveTrackToHistory(track)
                val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(convertTrackToJson(track))
                findNavController().navigate(action)
            }
        )

        setUpRecyclers(onElementClickAction)
        setTextWatcher()

        binding.searchScreenTextInputLayout.setEndIconOnClickListener {
            binding.searchScreenEditText.text?.clear()
            hideKeyboard()
            searchViewModel.cancelSearch()
            searchViewModel.showHistory()
        }

        binding.searchScreenEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) searchViewModel.showHistory()
        }

        searchViewModel.screenState.observe(viewLifecycleOwner) { state ->
            showScreenContent(state)
        }

        binding.searchScreenPlaceholderButton.setOnClickListener {
            if (binding.searchScreenEditText.text?.isNotEmpty() == true) {
                searchViewModel.searchTrack(binding.searchScreenEditText.text.toString())
            }
        }

        binding.searchScreenHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
        }

    }

    override fun onResume() {
        super.onResume()
        if (binding.searchScreenEditText.text?.isNotEmpty() == true) {
            binding.searchScreenTextInputLayout.isEndIconVisible = true
        }
    }

    override fun onStop() {
        super.onStop()
        binding.searchScreenEditText.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher = null
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

    private fun setUpRecyclers(onElementClickAction: (Track) -> Unit) {
        binding.searchScreenRecyclerViewTracks.adapter = adapter
        adapter.onListElementClick = onElementClickAction
        adapterForHistory.listOfTracks = SearchScreenState.History.tracks
        adapterForHistory.onListElementClick = onElementClickAction
        binding.searchScreenHistoryRecyclerView.adapter = adapterForHistory
    }

    private fun setTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().isNullOrBlank()) searchViewModel.searchDebounce(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNullOrBlank()) {
                    searchViewModel.cancelSearch()
                    searchViewModel.showHistory()
                }
            }
        }
        binding.searchScreenEditText.addTextChangedListener(textWatcher)
    }

    private fun showDefaultScreenState() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.GONE
            searchScreenPlaceholderButton.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun showLoading() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.GONE
            searchScreenProgressBar.visibility = View.VISIBLE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun showHistory() {
        with(binding) {
            searchScreenHistoryGroup.visibility =
                if (adapterForHistory.listOfTracks.isNotEmpty()) View.VISIBLE
                else View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenPlaceholder.visibility = View.GONE
        }
    }

    private fun showTracks() {
        with(binding) {
            searchScreenRecyclerViewTracks.visibility = View.VISIBLE
            searchScreenHistoryGroup.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenPlaceholder.visibility = View.GONE
        }
    }

    private fun showNotFound() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.VISIBLE
            searchScreenPlaceholderImage.setImageResource(R.drawable.img_not_found)
            searchScreenPlaceholderText.setText(getString(R.string.search_screen_placeholder_text_not_found))
            searchScreenPlaceholderButton.visibility = View.GONE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun showError() {
        with(binding) {
            searchScreenPlaceholder.visibility = View.VISIBLE
            searchScreenPlaceholderImage.setImageResource(R.drawable.img_no_internet)
            searchScreenPlaceholderText.setText(getString(R.string.search_screen_placeholder_text_error))
            searchScreenPlaceholderButton.visibility = View.VISIBLE
            searchScreenProgressBar.visibility = View.GONE
            searchScreenRecyclerViewTracks.visibility = View.GONE
            searchScreenHistoryGroup.visibility = View.GONE
        }
    }

    private fun convertTrackToJson(track: Track): String {
        return Gson().toJson(track)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}