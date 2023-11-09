package bes.max.playlistmaker.presentation.utils

import android.view.View
import androidx.constraintlayout.widget.Group

//fun for setting clickListener on a constraintlayout.widget.Group (for example on settings screen):
fun Group.setClickListenersForAllViews(listener: View.OnClickListener) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

