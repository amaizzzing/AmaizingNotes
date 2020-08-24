package com.amaizzzing.amaizingnotes.view.fragments.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.amaizzzing.amaizingnotes.R
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.util.*

class CalendarFragment : Fragment() {
    lateinit var cvCalenRangeFragmentCalendar:CardView
    lateinit var cv7dayRangeFragmentCalendar:CardView
    lateinit var cv14dayRangeFragmentCalendar:CardView
    lateinit var cvMonthRangeFragmentCalendar:CardView

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        initViews(root)

        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        calendarViewModel.getMyRange7Days()?.observe(viewLifecycleOwner,Observer{
            Toast.makeText(context,"${it.size}",Toast.LENGTH_LONG).show()
        })

        return root
    }

    private fun initViews(v:View){
        cvCalenRangeFragmentCalendar = v.cv_calen_range_fragment_calendar
        cv7dayRangeFragmentCalendar = v.cv_7day_range_fragment_calendar
        cv14dayRangeFragmentCalendar = v.cv_14day_range_fragment_calendar
        cvMonthRangeFragmentCalendar = v.cv_month_range_fragment_calendar
    }

    override fun onStart() {
        super.onStart()
        calendarViewModel.getMyRange7Days()
    }
}
