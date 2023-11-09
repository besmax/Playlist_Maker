package bes.max.playlistmaker.presentation.mediateka.editplaylist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.domain.models.Playlist
import bes.max.playlistmaker.presentation.mediateka.newplaylist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : NewPlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistScreenBackArrow.setTitle(R.string.edit)

        binding.newPlaylistScreenButton.text = getString(R.string.save)

        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            when (playlist) {
                is EditPlaylistScreenState.Editing -> bindViews(playlist.playlist)
                is EditPlaylistScreenState.Updated -> findNavController().navigateUp()
            }
        }

        binding.newPlaylistScreenBackArrow.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.newPlaylistScreenButton.setOnClickListener {
            updatePlaylist()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun bindViews(playlist: Playlist) {
        playlist.coverPath?.let { setImageToView(it.toUri()) }
        binding.newPlaylistScreenNameInput.setText(playlist.name)
        binding.newPlaylistScreenDescriptionInput.setText(
            playlist.description
        )
    }

    private fun updatePlaylist() {
        val description = if (binding.newPlaylistScreenDescriptionInput.text.toString().isNotBlank()
            && binding.newPlaylistScreenDescriptionInput.text.toString() != (viewModel.playlist.value as EditPlaylistScreenState.Editing).playlist.description
        )
            binding.newPlaylistScreenDescriptionInput.text.toString()
        else null
        viewModel.updatePlaylist(
            name = binding.newPlaylistScreenNameInput.text.toString(),
            description = description,
            uri = coverUri
        )
    }

}