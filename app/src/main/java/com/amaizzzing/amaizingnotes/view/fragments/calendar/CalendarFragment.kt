package com.amaizzzing.amaizingnotes.view.fragments.calendar

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.view.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CalendarFragment : Fragment() {
    lateinit var cvTodayRangeFragmentCalendar: CardView
    lateinit var cv7dayRangeFragmentCalendar: CardView
    lateinit var cv14dayRangeFragmentCalendar: CardView
    lateinit var cvMonthRangeFragmentCalendar: CardView
    lateinit var rvFragmentCalendar: RecyclerView
    lateinit var fabButtonFragmentCalendar:FloatingActionButton

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
        initListeners()

        previousRange=670211000L
        calendarViewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        calendarViewModel.getMyRangeDays(0L)?.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeDaysCardsColors(cvTodayRangeFragmentCalendar)
    }

    fun initListeners(){
        cv7dayRangeFragmentCalendar.setOnClickListener {
            changeDaysCardsColors(cv7dayRangeFragmentCalendar)
            calendarViewModel.getMyRangeDays(670211000L)?.observe(viewLifecycleOwner, Observer {
                initRecyclerView(it)
            })
        }
        cv14dayRangeFragmentCalendar.setOnClickListener {
            changeDaysCardsColors(cv14dayRangeFragmentCalendar)
            calendarViewModel.getMyRangeDays(1340422000L)?.observe(viewLifecycleOwner, Observer {
                initRecyclerView(it)
            })
        }
        cvMonthRangeFragmentCalendar.setOnClickListener {
            changeDaysCardsColors(cvMonthRangeFragmentCalendar)
            calendarViewModel.getMyRangeDays(2872332857L)?.observe(viewLifecycleOwner, Observer {
                initRecyclerView(it)
            })
        }
        cvTodayRangeFragmentCalendar.setOnClickListener {
            changeDaysCardsColors(cvTodayRangeFragmentCalendar)
            calendarViewModel.getMyRangeDays(0L)?.observe(viewLifecycleOwner, Observer {
                initRecyclerView(it)
            })
        }

        fabButtonFragmentCalendar.setOnClickListener {
            navController.navigate(R.id.action_navigation_calendar_to_navigation_add_notes)
        }
    }

    fun changeDaysCardsColors(currentView:CardView){
        currentView.setCardBackgroundColor(resources.getColor(R.color.transPrimary))
        if(cvTodayRangeFragmentCalendar!=currentView)
            cvTodayRangeFragmentCalendar.setCardBackgroundColor(resources.getColor(R.color.invisible_color))
        if(cv7dayRangeFragmentCalendar!=currentView)
            cv7dayRangeFragmentCalendar.setCardBackgroundColor(resources.getColor(R.color.invisible_color))
        if(cv14dayRangeFragmentCalendar!=currentView)
            cv14dayRangeFragmentCalendar.setCardBackgroundColor(resources.getColor(R.color.invisible_color))
        if(cvMonthRangeFragmentCalendar!=currentView)
            cvMonthRangeFragmentCalendar.setCardBackgroundColor(resources.getColor(R.color.invisible_color))

    }

    private fun initViews(v: View) {
        cvTodayRangeFragmentCalendar = v.cv_today_range_fragment_calendar
        cv7dayRangeFragmentCalendar = v.cv_7day_range_fragment_calendar
        cv14dayRangeFragmentCalendar = v.cv_14day_range_fragment_calendar
        cvMonthRangeFragmentCalendar = v.cv_month_range_fragment_calendar
        rvFragmentCalendar = v.rv_fragment_calendar
        fabButtonFragmentCalendar = v.fab_button_fragment_calendar
    }

    override fun onStart() {
        super.onStart()
        calendarViewModel.getMyRangeDays(0L)?.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })
    }

    fun initRecyclerView(notesList: List<Note>) {
        todayNotesAdapter = TodayNotesAdapter(notesList, object : TodayNotesAdapter.Callback {
            override fun onItemClicked(item: Note) {
                val bundle = Bundle()
                bundle.putLong(getString(R.string.current_note), item.id)
                navController.navigate(R.id.action_navigation_calendar_to_navigation_add_notes, bundle)
            }

            override fun onDeleteClicked(item: Note) {
                val noteDao = NotesApplication.instance.getAppNoteDao()
                with(CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
                    launch {
                        noteDao?.deleteNote(NoteMapper.noteToApiNote(item))
                    }
                }
            }

            override fun onChcbxChecked(item: Note) {
                item.isDone = !item.isDone
                with(CoroutineScope(SupervisorJob() + Dispatchers.IO)){
                    launch {
                        calendarViewModel.updateNote(item)
                    }
                }
            }
        })
        rvFragmentCalendar.layoutManager = GridLayoutManager(context,2)
        /*rvFragmentCalendar.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )*/
        rvFragmentCalendar.adapter = todayNotesAdapter
    }
}
