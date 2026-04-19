package au.edu.swin.passtask2a_wishyouwerehere

import android.view.View
import android.widget.AdapterView

class SimpleItemSelectedListener(
    private val onItemSelectedAction: () -> Unit
) : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        onItemSelectedAction()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
}

