package bes.max.playlistmaker.presentation.mediateka.newplaylist

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


open class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private var textWatcher: TextWatcher? = null
    protected open val viewModel: NewPlaylistViewModel by viewModel()
    protected var coverUri: Uri? = null

    private var defaultDrawable: Drawable? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                setImageToView(uri)
                coverUri = uri
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

        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is NewPlaylistScreenState.Created -> findNavController().navigateUp()
                else -> {}
            }
        }

        binding.newPlaylistScreenBackArrow.setNavigationOnClickListener {
            showSaveBeforeExitDialog()
        }

        binding.newPlaylistScreenPlaylistCover.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlaylistScreenButton.setOnClickListener {
            savePlaylist()
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
            val alert =
                MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
                    .setTitle(getString(R.string.newplaylist_screen_dialog_title))
                    .setMessage(getString(R.string.newplaylist_screen_dialog_message))
                    .setNeutralButton(getString(R.string.Cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.Complete)) { dialog, _ ->
                        dialog.dismiss()
                        findNavController().navigateUp()
                    }
            alert.show()
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
        viewModel.createPlaylist(
            name = name,
            description = binding.newPlaylistScreenDescriptionInput.text.toString(),
            trackArg = arguments?.getString("track"),
            uri = coverUri
        )
        Log.d(TAG, "save playlist with uri: ${coverUri.toString()}")
        findNavController().previousBackStackEntry?.savedStateHandle?.set("playlistName", name)
    }

    protected fun setImageToView(uri: Uri) {
        Glide.with(binding.root)
            .load(uri)
            .centerCrop()
            .placeholder(R.drawable.ic_picture_not_found)
            .into(binding.newPlaylistScreenPlaylistCover)
    }

    companion object {
        private const val TAG = "NewPlaylistFragment"
    }

}