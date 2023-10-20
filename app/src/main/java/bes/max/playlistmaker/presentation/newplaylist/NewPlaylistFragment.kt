package bes.max.playlistmaker.presentation.newplaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import bes.max.playlistmaker.databinding.FragmentNewPlaylistBinding
import bes.max.playlistmaker.presentation.utils.BindingFragment


class NewPlaylistFragment : BindingFragment<FragmentNewPlaylistBinding>() {

    private lateinit var textWatcher: TextWatcher
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
    }

    private fun setTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.newPlaylistScreenButton.isEnabled = s?.isNotBlank() == true
            }
        }
        binding.newPlaylistScreenNameInput.addTextChangedListener(textWatcher)
    }


}