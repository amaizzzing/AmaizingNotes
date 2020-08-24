package com.amaizzzing.amaizingnotes.view.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

class CalendarFragment : Fragment() {
    lateinit var cvCalenRangeFragmentCalendar: CardView
    lateinit var cv7dayRangeFragmentCalendar: CardView
    lateinit var cv14dayRangeFragmentCalendar: CardView
    lateinit var cvMonthRangeFragmentCalendar: CardView
    lateinit var rvFragmentCalendar: RecyclerView
    lateinit var todayNotesAdapter: TodayNotesAdapter
    lateinit var navController: NavController

    private lateinit var calendarViewModel: CalendarViewModel

    private var previousRange = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        initViews(root)

        previousRange=670211000L
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        calendarViewModel.getMyRangeDays(670211000L)?.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    private fun initViews(v: View) {
        cvCalenRangeFragmentCalendar = v.cv_calen_range_fragment_calendar
        cv7dayRangeFragmentCalendar = v.cv_7day_range_fragment_calendar
        cv14dayRangeFragmentCalendar = v.cv_14day_range_fragment_calendar
        cvMonthRangeFragmentCalendar = v.cv_month_range_fragment_calendar
        rvFragmentCalendar = v.rv_fragment_calendar
    }

    override fun onStart() {
        super.onStart()
        calendarViewModel.getMyRangeDays(previousRange)?.removeObservers(this)
        calendarViewModel.getMyRangeDays(0L)?.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
    }

    fun initRecyclerView(notesList: List<Note>) {
        todayNotesAdapter = TodayNotesAdapter(notesList, object : TodayNotesAdapter.Callback {
            override fun onItemClicked(item: Note) {
                val bundle = Bundle()
                bundle.putLong(getString(R.string.current_note), item.id)
                navController.navigate(R.id.action_navigation_home_to_navigation_add_notes, bundle)
            }

            override fun onDeleteClicked(item: Note) {
                val noteDao = NotesApplication.instance.getAppNoteDao()
                with(CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
                    launch {
                        noteDao?.deleteNode(NoteMapper.noteToApiNote(item))
                    }
                }
            }
        })
        rvFragmentCalendar.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        rvFragmentCalendar.adapter = todayNotesAdapter
    }
}
