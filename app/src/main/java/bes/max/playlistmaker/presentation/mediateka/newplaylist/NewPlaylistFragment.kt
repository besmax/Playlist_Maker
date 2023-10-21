package bes.max.playlistmaker.presentation.mediateka.newplaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.databinding.FragmentNewPlaylistBinding
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.presentation.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private var textWatcher: TextWatcher? = null
    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()

    private val imagePicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.newPlaylistScreenPlaylistCover.setImageURI(uri)
            newPlaylistViewModel.coverUri = uri
        } else {
            Log.d(TAG, "No image selected for playlist cover")
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewPlaylistBinding {
        return FragmentNewPlaylistBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextWatcher()

        binding.newPlaylistScreenBackArrow.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.newPlaylistScreenPlaylistCover.setOnClickListener {
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlaylistScreenButton.setOnClickListener {
            savePlaylist()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher = null
    }

    private fun setTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.newPlaylistScreenButton.isEnabled = s?.isNotBlank() == true
            }
        }
        binding.newPlaylistScreenNameInput.addTextChangedListener(textWatcher)
    }

    private fun savePlaylist() {
        newPlaylistViewModel.createPlaylist(
            name = binding.newPlaylistScreenNameInput.text.toString(),
            description = binding.newPlaylistScreenDescriptionInput.text.toString(),
        )
    }

    companion object {
        private const val TAG = "NewPlaylistFragment"
    }

}