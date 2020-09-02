package com.amaizzzing.amaizingnotes.view.fragments

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
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.components.DaggerDiNotesComponent
import com.amaizzzing.amaizingnotes.model.di.modules.*
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepositoryImpl
import com.amaizzzing.amaizingnotes.utils.*
import com.amaizzzing.amaizingnotes.viewmodel.CalendarViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import javax.inject.Inject

class CalendarFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var calendarViewModel: CalendarViewModel

    private lateinit var cvTodayRangeFragmentCalendar: CardView
    private lateinit var cv7dayRangeFragmentCalendar: CardView
    private lateinit var cv14dayRangeFragmentCalendar: CardView
    private lateinit var cvMonthRangeFragmentCalendar: CardView
    private lateinit var rvFragmentCalendar: RecyclerView
    private lateinit var fabButtonFragmentCalendar: FloatingActionButton

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

        calendarViewModel = ViewModelProvider(this,factory).get(CalendarViewModel::class.java)
        calendarViewModel.someDaysLiveData.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    private fun initCardAndGetDays(v: CardView, range: Long) {
        changeDaysCardsColors(v)
        calendarViewModel.getSomeDays(range)
    }

    private fun initListeners() {
        cv7dayRangeFragmentCalendar.setOnClickListener {
            initCardAndGetDays(cv7dayRangeFragmentCalendar, DAYS_7_IN_MILLIS)
        }
        cv14dayRangeFragmentCalendar.setOnClickListener {
            initCardAndGetDays(cv14dayRangeFragmentCalendar, DAYS_14_IN_MILLIS)
        }
        cvMonthRangeFragmentCalendar.setOnClickListener {
            initCardAndGetDays(cvMonthRangeFragmentCalendar, DAYS_30_IN_MILLIS)
        }
        cvTodayRangeFragmentCalendar.setOnClickListener {
            initCardAndGetDays(cvTodayRangeFragmentCalendar, DAYS_0_IN_MILLIS)
        }

        fabButtonFragmentCalendar.setOnClickListener {
            navController.navigate(R.id.action_navigation_calendar_to_navigation_add_notes)
        }
    }

    private fun changeDaysCardsColors(currentView: CardView) {
        cvTodayRangeFragmentCalendar.setCardBackgroundColor(
            resources.getColor(
                R.color.invisible_color,
                activity?.theme
            )
        )
        cv7dayRangeFragmentCalendar.setCardBackgroundColor(
            resources.getColor(
                R.color.invisible_color,
                activity?.theme
            )
        )
        cv14dayRangeFragmentCalendar.setCardBackgroundColor(
            resources.getColor(
                R.color.invisible_color,
                activity?.theme
            )
        )
        cvMonthRangeFragmentCalendar.setCardBackgroundColor(
            resources.getColor(
                R.color.invisible_color,
                activity?.theme
            )
        )
        when (currentView) {
            cvTodayRangeFragmentCalendar -> cvTodayRangeFragmentCalendar.setCardBackgroundColor(
                resources.getColor(R.color.transPrimary, activity?.theme)
            )
            cv7dayRangeFragmentCalendar -> cv7dayRangeFragmentCalendar.setCardBackgroundColor(
                resources.getColor(R.color.transPrimary, activity?.theme)
            )
            cv14dayRangeFragmentCalendar -> cv14dayRangeFragmentCalendar.setCardBackgroundColor(
                resources.getColor(R.color.transPrimary, activity?.theme)
            )
            cvMonthRangeFragmentCalendar -> cvMonthRangeFragmentCalendar.setCardBackgroundColor(
                resources.getColor(R.color.transPrimary, activity?.theme)
            )
        }
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
        initCardAndGetDays(cvTodayRangeFragmentCalendar, DAYS_0_IN_MILLIS)
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
