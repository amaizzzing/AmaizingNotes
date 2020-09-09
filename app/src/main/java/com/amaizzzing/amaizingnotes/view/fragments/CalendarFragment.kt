package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.utils.DAYS_0_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.DAYS_30_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.DAYS_7_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.SPAN_COUNT_RV
import com.amaizzzing.amaizingnotes.view.base.BaseViewState
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.amaizzzing.amaizingnotes.viewmodel.CalendarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import javax.inject.Inject

class CalendarFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var calendarViewModel: CalendarViewModel

    private lateinit var rvFragmentCalendar: RecyclerView
    private lateinit var fabButtonFragmentCalendar: FloatingActionButton
    private lateinit var pbFragmentCalendar: ProgressBar
    private lateinit var menuCalendarFragment:ImageView
    private lateinit var llChooseRangeFragmentCalendar:LinearLayout
    private lateinit var radbutAllCalendarFragment:RadioButton
    private lateinit var radbutNotesCalendarFragment:RadioButton
    private lateinit var radbutTasksCalendarFragment:RadioButton
    private lateinit var radbutTodayCalendarFragment:RadioButton
    private lateinit var radbutWeekCalendarFragment:RadioButton
    private lateinit var radbutMonthCalendarFragment:RadioButton
    private lateinit var radbutAlltimeCalendarFragment:RadioButton
    private lateinit var filterFragmentCalendar:ImageView
    private lateinit var searchNoteFragmentCalendar:SearchView

    private lateinit var todayNotesAdapter: TodayNotesAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)

        initViews(root)
        initListeners()

        val comp2 = DaggerComponent2.builder()
            .clearModule(ClearModule())
            .build()
        comp2.injectToCalendarFragment(this)

        calendarViewModel = ViewModelProvider(this, factory).get(CalendarViewModel::class.java)
        calendarViewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer {
            renderUI(it)
        })

        return root
    }

    private fun renderUI(noteViewState: BaseViewState<MutableList<Note>>) {
        renderError(noteViewState.error)
        renderProgress(noteViewState.isLoading)
        renderNoteList(noteViewState.data)
    }

    private fun renderNoteList(notes: List<Note>?) {
        if (notes != null) {
            initRecyclerView(notes)
        }
    }

    private fun renderProgress(loading: Boolean) {
        if (loading) {
            pbFragmentCalendar.visibility = View.VISIBLE
            rvFragmentCalendar.visibility = View.GONE
        } else {
            pbFragmentCalendar.visibility = View.GONE
            rvFragmentCalendar.visibility = View.VISIBLE
        }
    }

    private fun renderError(error: Throwable?) {
        if (error != null) {
            Toast.makeText(context, "${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    private fun initListeners() {
        radbutAllCalendarFragment.setOnClickListener {
            getNotesByNoteType(NoteType.ALL)
        }
        radbutNotesCalendarFragment.setOnClickListener {
            getNotesByNoteType(NoteType.NOTE)
        }
        radbutTasksCalendarFragment.setOnClickListener {
            getNotesByNoteType(NoteType.TASK)
        }
        radbutTodayCalendarFragment.setOnClickListener {
            geNotesByRange(radbutTodayCalendarFragment)
        }
        radbutWeekCalendarFragment.setOnClickListener {
            geNotesByRange(radbutWeekCalendarFragment)
        }
        radbutMonthCalendarFragment.setOnClickListener {
            geNotesByRange(radbutMonthCalendarFragment)
        }
        radbutAlltimeCalendarFragment.setOnClickListener {
            geNotesByRange(radbutAlltimeCalendarFragment)
        }
        menuCalendarFragment.setOnClickListener {
                val pop= PopupMenu(it.context, it)
                pop.inflate(R.menu.popup_menu_calendar_fragment)
                pop.show()
        }

        fabButtonFragmentCalendar.setOnClickListener {
            navController.navigate(R.id.action_navigation_calendar_to_navigation_add_notes)
        }

        filterFragmentCalendar.setOnClickListener {
            if(llChooseRangeFragmentCalendar.visibility==View.GONE){
                llChooseRangeFragmentCalendar.visibility = View.VISIBLE
            }else{
                llChooseRangeFragmentCalendar.visibility=View.GONE
            }
        }

        searchNoteFragmentCalendar.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                calendarViewModel.searchNotes(newText!!)
                return true
            }

        })
    }

    private fun getNotesByNoteType(noteType: NoteType) {
        if(noteType==NoteType.ALL){
            val range =
                when{
                    radbutTodayCalendarFragment.isChecked -> getRangeForFilter(
                        radbutTodayCalendarFragment
                    )
                    radbutWeekCalendarFragment.isChecked -> getRangeForFilter(
                        radbutWeekCalendarFragment
                    )
                    radbutMonthCalendarFragment.isChecked -> getRangeForFilter(
                        radbutMonthCalendarFragment
                    )
                    radbutAlltimeCalendarFragment.isChecked -> getRangeForFilter(
                        radbutAlltimeCalendarFragment
                    )
                    else -> 0L
                }
            calendarViewModel.getSomeDays(range,NoteType.ALL)
        }else {
            when {
                radbutTodayCalendarFragment.isChecked -> calendarViewModel.getSomeDays(
                    getRangeForFilter(
                        radbutTodayCalendarFragment
                    ), noteType
                )
                radbutWeekCalendarFragment.isChecked -> calendarViewModel.getSomeDays(
                    getRangeForFilter(
                        radbutWeekCalendarFragment
                    ), noteType
                )
                radbutMonthCalendarFragment.isChecked -> calendarViewModel.getSomeDays(
                    getRangeForFilter(
                        radbutMonthCalendarFragment
                    ), noteType
                )
                radbutAlltimeCalendarFragment.isChecked -> calendarViewModel.getSomeDays(
                    getRangeForFilter(
                        radbutAlltimeCalendarFragment
                    ), noteType
                )
            }
        }
    }

    private fun geNotesByRange(radioButton: RadioButton) {
        when {
            radbutAllCalendarFragment.isChecked -> calendarViewModel.getSomeDays(
                getRangeForFilter(
                    radioButton
                ), NoteType.ALL
            )
            radbutNotesCalendarFragment.isChecked -> calendarViewModel.getSomeDays(
                getRangeForFilter(
                    radioButton
                ), NoteType.NOTE
            )
            radbutTasksCalendarFragment.isChecked -> calendarViewModel.getSomeDays(
                getRangeForFilter(
                    radioButton
                ), NoteType.TASK
            )
        }
    }

    private fun getRangeForFilter(radioButton: RadioButton):Long =
        when(radioButton){
            radbutAlltimeCalendarFragment -> Int.MAX_VALUE.toLong()
            radbutTodayCalendarFragment -> DAYS_0_IN_MILLIS
            radbutWeekCalendarFragment -> DAYS_7_IN_MILLIS
            radbutMonthCalendarFragment -> DAYS_30_IN_MILLIS
            else -> 0L
        }

    private fun initViews(v: View) {
        menuCalendarFragment = v.menu_fragment_calendar
        rvFragmentCalendar = v.rv_fragment_calendar
        fabButtonFragmentCalendar = v.fab_button_fragment_calendar
        pbFragmentCalendar = v.pb_fragment_calendar
        llChooseRangeFragmentCalendar = v.ll_choose_range_fragment_calendar
        radbutAllCalendarFragment = v.radbut_all_calendar_fragment
        radbutNotesCalendarFragment = v.radbut_notes_calendar_fragment
        radbutTasksCalendarFragment = v.radbut_tasks_calendar_fragment
        radbutTodayCalendarFragment = v.radbut_today_calendar_fragment
        radbutWeekCalendarFragment = v.radbut_week_calendar_fragment
        radbutMonthCalendarFragment = v.radbut_month_calendar_fragment
        radbutAlltimeCalendarFragment = v.radbut_alltime_calendar_fragment
        filterFragmentCalendar = v.filter_fragment_calendar
        searchNoteFragmentCalendar = v.search_note_fragment_calendar
    }

    override fun onStart() {
        super.onStart()
        calendarViewModel.getSomeDays(0L,NoteType.ALL)
    }

    private fun initRecyclerView(notesList: List<Note>) {
        todayNotesAdapter = TodayNotesAdapter(notesList, object : TodayNotesAdapter.Callback {
            override fun onItemClicked(item: Note) {
                val bundle = Bundle()
                bundle.putLong(getString(R.string.current_note), item.id)
                navController.navigate(
                    R.id.action_navigation_calendar_to_navigation_add_notes,
                    bundle
                )
            }

            override fun onChcbxChecked(item: Note, isChecked: Boolean) {
                item.isDone = isChecked
                calendarViewModel.updateNote(item)
            }
        })
        rvFragmentCalendar.layoutManager = GridLayoutManager(context, SPAN_COUNT_RV)
        todayNotesAdapter.items = notesList
        rvFragmentCalendar.adapter = todayNotesAdapter
    }
}
