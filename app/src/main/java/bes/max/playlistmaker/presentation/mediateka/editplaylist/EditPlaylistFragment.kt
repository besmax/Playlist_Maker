package bes.max.playlistmaker.presentation.mediateka.editplaylist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.R
import bes.max.playlistmaker.presentation.mediateka.newplaylist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class EditPlaylistFragment : NewPlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistScreenBackArrow.setTitle(R.string.edit)

        binding.newPlaylistScreenButton.text = getString(R.string.save)

        bindViews()

        binding.newPlaylistScreenBackArrow.setNavigationOnClickListener {
            findNavController().navigateUp()
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

    private fun bindViews() {
        viewModel.playlist.value?.coverPath?.let { setImageToView(it.toUri()) }
        binding.newPlaylistScreenNameInput.setText(viewModel.playlist.value?.name ?: "")
        binding.newPlaylistScreenDescriptionInput.setText(
            viewModel.playlist.value?.description ?: ""
        )
    }

}