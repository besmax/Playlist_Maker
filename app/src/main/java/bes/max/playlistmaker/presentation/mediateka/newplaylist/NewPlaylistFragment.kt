package bes.max.playlistmaker.presentation.mediateka.newplaylist

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bes.max.playlistmaker.R
import bes.max.playlistmaker.databinding.FragmentNewPlaylistBinding
import bes.max.playlistmaker.presentation.utils.BindingFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private var textWatcher: TextWatcher? = null
    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()
    private val safeArgs: NewPlaylistFragmentArgs by navArgs()

    private var defaultDrawable: Drawable? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.newPlaylistScreenPlaylistCover.setImageURI(uri)
                newPlaylistViewModel.saveImageToPrivateStorage(uri)
            } else {
                Log.d(TAG, "No image selected for playlist cover")
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showSaveBeforeExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
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
            showSaveBeforeExitDialog()
        }

        binding.newPlaylistScreenPlaylistCover.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlaylistScreenButton.setOnClickListener {
            savePlaylist()
            findNavController().navigateUp()
        }

        defaultDrawable = binding.newPlaylistScreenPlaylistCover.drawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher = null
    }

    private fun showSaveBeforeExitDialog() {
        if (!binding.newPlaylistScreenNameInput.text.isNullOrBlank() ||
            !binding.newPlaylistScreenDescriptionInput.text.isNullOrBlank() ||
            binding.newPlaylistScreenPlaylistCover.drawable != defaultDrawable
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.newplaylist_screen_dialog_title))
                .setMessage(getString(R.string.newplaylist_screen_dialog_message))
                .setNeutralButton(getString(R.string.Cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.Complete)) { dialog, _ ->
                    dialog.dismiss()
                    findNavController().navigateUp()
                }
                .show()
        } else {
            findNavController().navigateUp()
        }
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
        val name = binding.newPlaylistScreenNameInput.text.toString()
        newPlaylistViewModel.createPlaylist(
            name = name,
            description = binding.newPlaylistScreenDescriptionInput.text.toString(),
            trackArg = safeArgs.track,
        )
        findNavController().previousBackStackEntry?.savedStateHandle?.set("playlistName", name)
    }

    companion object {
        private const val TAG = "NewPlaylistFragment"
    }

}