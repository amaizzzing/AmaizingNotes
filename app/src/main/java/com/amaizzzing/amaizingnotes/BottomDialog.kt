package com.amaizzzing.amaizingnotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.adapters.BottomDialogAdapter
import com.amaizzzing.amaizingnotes.utils.SPAN_COUNT_RV
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class BottomDialog : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

        val rvBottomDialog = view.rv_bottom_dialog

        var bottomDialogAdapter = BottomDialogAdapter(listOf(R.drawable.ic_back_24,R.drawable.ic_calendar_24dp))
        rvBottomDialog.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        rvBottomDialog.adapter = bottomDialogAdapter

        return view;
    }

    fun newInstance() = BottomDialog()

}