package com.amaizzzing.amaizingnotes.view.dialogs

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.amaizzzing.amaizingnotes.R

class LogoutDialog : DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + " TAG"

        fun createInstance(onLogout: (() -> Unit)) = LogoutDialog().apply {
            this.onLogout = onLogout
        }
    }

    var onLogout: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?) = AlertDialog.Builder(requireContext())
        .setTitle(R.string.logout_title)
        .setMessage(R.string.logout_message)
        .setPositiveButton(R.string.yes) { _, _ -> onLogout?.invoke() }
        .setNegativeButton(R.string.no) { _, _ -> dismiss() }
        .create()
}
