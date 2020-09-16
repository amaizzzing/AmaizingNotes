package com.amaizzzing.amaizingnotes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.adapters.BottomDialogAdapter
import com.amaizzzing.amaizingnotes.utils.DATE_TYPE
import com.amaizzzing.amaizingnotes.view.custom_view.ColorPickerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*


class BottomDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

        val rvBottomDialog = view.rv_bottom_dialog

        var bottomDialogAdapter = BottomDialogAdapter(
            listOf(
                R.drawable.back_note_1,
                R.drawable.back_note_2,
                R.drawable.back_note_3,
                R.drawable.back_note_4,
                R.drawable.back_note_5,
                R.drawable.back_note_6,
                R.drawable.back_note_7),
            object : BottomDialogAdapter.Callback {
                override fun onItemClicked(item: Int) {
                    val intent = Intent()
                    intent.putExtra(DATE_TYPE, item)
                    targetFragment!!.onActivityResult(targetRequestCode, 1, intent)
                    dismiss()
                }

            }
        )

        var colorPicker_view_bottom_dialog : ColorPickerView= view.findViewById(R.id.color_picker_view_bottom_dialog)
        colorPicker_view_bottom_dialog.onColorClickListener = {
            val intent = Intent()
            intent.putExtra(DATE_TYPE, it.color)
            targetFragment!!.onActivityResult(targetRequestCode, 1, intent)
            dismiss()
        }

        rvBottomDialog.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
        rvBottomDialog.adapter = bottomDialogAdapter

        return view;
    }

    fun newInstance() = BottomDialog()

}