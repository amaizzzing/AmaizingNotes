package com.amaizzzing.amaizingnotes.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.utils.DAYS_0_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.DAYS_30_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.DAYS_7_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.SPAN_COUNT_RV
import com.amaizzzing.amaizingnotes.view.activities.SplashActivity
import com.amaizzzing.amaizingnotes.view.base.BaseFragment
import com.amaizzzing.amaizingnotes.view.dialogs.LogoutDialog
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.amaizzzing.amaizingnotes.viewmodel.CalendarViewModel
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CalendarFragment :
    BaseFragment<MutableList<Note>, CalendarNoteViewState<MutableList<Note>>>() {
    override val viewModel: CalendarViewModel by viewModel()
    val navController by lazy {
        findNavController()
    }
    override val layoutRes: Int = R.layout.fragment_calendar
    override val rootView: View by lazy {
        this.layoutInflater.inflate(R.layout.fragment_calendar, container, false)
    }

    private val rvFragmentCalendar: RecyclerView by lazy {
        rootView.rv_fragment_calendar
    }

    private lateinit var todayNotesAdapter: TodayNotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return rootView
    }

    override fun renderUI(data: CalendarNoteViewState<MutableList<Note>>) {
        renderError(data.error)
        renderProgress(data.isLoading)
        renderNoteList(data.data)
    }

    private fun renderError(error: Throwable?) {
        error?.let {
            Toast.makeText(context, "ERROR!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderNoteList(notes: List<Note>?) {
        notes?.let {initRecyclerView(notes)} ?: initRecyclerView(listOf())
    }

    private fun renderProgress(loading: Boolean) {
        if (loading) {
            rootView.pb_fragment_calendar.visibility = View.VISIBLE
            rvFragmentCalendar.visibility = View.GONE
        } else {
            rootView.pb_fragment_calendar.visibility = View.GONE
            rvFragmentCalendar.visibility = View.VISIBLE
        }
    }

    override fun initListeners() {
        rootView.radbut_all_calendar_fragment.setOnClickListener {
            getNotesByNoteType(NoteType.ALL)
        }
        rootView.radbut_notes_calendar_fragment.setOnClickListener {
            getNotesByNoteType(NoteType.NOTE)
        }
        rootView.radbut_tasks_calendar_fragment.setOnClickListener {
            getNotesByNoteType(NoteType.TASK)
        }
        rootView.radbut_today_calendar_fragment.setOnClickListener {
            geNotesByRange(rootView.radbut_today_calendar_fragment)
        }
        rootView.radbut_week_calendar_fragment.setOnClickListener {
            geNotesByRange(rootView.radbut_week_calendar_fragment)
        }
        rootView.radbut_month_calendar_fragment.setOnClickListener {
            geNotesByRange(rootView.radbut_month_calendar_fragment)
        }
        rootView.radbut_alltime_calendar_fragment.setOnClickListener {
            geNotesByRange(rootView.radbut_alltime_calendar_fragment)
        }
        rootView.menu_fragment_calendar.setOnClickListener {
            val pop = PopupMenu(it.context, it)
            pop.inflate(R.menu.popup_menu_calendar_fragment)
            pop.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.settings -> {
                        Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.log_out -> {
                        showLogoutDialog().let { true }
                    }
                }
                true
            }
            pop.show()
        }

        rootView.fab_button_fragment_calendar.setOnClickListener {
            navController.navigate(R.id.action_navigation_calendar_to_navigation_add_notes)
        }

        rootView.filter_fragment_calendar.setOnClickListener {
            if (rootView.ll_choose_range_fragment_calendar.visibility == View.GONE) {
                rootView.ll_choose_range_fragment_calendar.visibility = View.VISIBLE
            } else {
                rootView.ll_choose_range_fragment_calendar.visibility = View.GONE
            }
        }

        rootView.search_note_fragment_calendar.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchNotes(newText!!)
                return true
            }
        })
    }

    private fun showLogoutDialog() {
        parentFragmentManager.findFragmentByTag(LogoutDialog.TAG) ?: LogoutDialog.createInstance {
            onLogout()
        }.show(parentFragmentManager, LogoutDialog.TAG)
    }

    private fun onLogout() {
        AuthUI.getInstance()
            .signOut(requireContext())
            .addOnCompleteListener {
                startActivity(Intent(requireContext(), SplashActivity::class.java))
            }
    }

    private fun getNotesByNoteType(noteType: NoteType) {
        if (noteType == NoteType.ALL) {
            val range =
                when {
                    rootView.radbut_today_calendar_fragment.isChecked -> getRangeForFilter(
                        rootView.radbut_today_calendar_fragment
                    )
                    rootView.radbut_week_calendar_fragment.isChecked -> getRangeForFilter(
                        rootView.radbut_week_calendar_fragment
                    )
                    rootView.radbut_month_calendar_fragment.isChecked -> getRangeForFilter(
                        rootView.radbut_month_calendar_fragment
                    )
                    rootView.radbut_alltime_calendar_fragment.isChecked -> getRangeForFilter(
                        rootView.radbut_alltime_calendar_fragment
                    )
                    else -> 0L
                }
            viewModel.getSomeDays(range, NoteType.ALL)
        } else {
            when {
                rootView.radbut_today_calendar_fragment.isChecked -> viewModel.getSomeDays(
                    getRangeForFilter(
                        rootView.radbut_today_calendar_fragment
                    ), noteType
                )
                rootView.radbut_week_calendar_fragment.isChecked -> viewModel.getSomeDays(
                    getRangeForFilter(
                        rootView.radbut_week_calendar_fragment
                    ), noteType
                )
                rootView.radbut_month_calendar_fragment.isChecked -> viewModel.getSomeDays(
                    getRangeForFilter(
                        rootView.radbut_month_calendar_fragment
                    ), noteType
                )
                rootView.radbut_alltime_calendar_fragment.isChecked -> viewModel.getSomeDays(
                    getRangeForFilter(
                        rootView.radbut_alltime_calendar_fragment
                    ), noteType
                )
            }
        }
    }

    private fun geNotesByRange(radioButton: RadioButton) {
        when {
            rootView.radbut_all_calendar_fragment.isChecked -> viewModel.getSomeDays(
                getRangeForFilter(
                    radioButton
                ), NoteType.ALL
            )
            rootView.radbut_notes_calendar_fragment.isChecked -> viewModel.getSomeDays(
                getRangeForFilter(
                    radioButton
                ), NoteType.NOTE
            )
            rootView.radbut_tasks_calendar_fragment.isChecked -> viewModel.getSomeDays(
                getRangeForFilter(
                    radioButton
                ), NoteType.TASK
            )
        }
    }

    private fun getRangeForFilter(radioButton: RadioButton): Long =
        when (radioButton) {
            rootView.radbut_alltime_calendar_fragment -> Int.MAX_VALUE.toLong()
            rootView.radbut_today_calendar_fragment -> DAYS_0_IN_MILLIS
            rootView.radbut_week_calendar_fragment -> DAYS_7_IN_MILLIS
            rootView.radbut_month_calendar_fragment -> DAYS_30_IN_MILLIS
            else -> 0L
        }

    override fun onStart() {
        super.onStart()
        viewModel.getSomeDays(0L, NoteType.ALL)
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
                viewModel.updateNote(item)
            }
        })
        rvFragmentCalendar.layoutManager = GridLayoutManager(context, SPAN_COUNT_RV)
        todayNotesAdapter.items = notesList
        rvFragmentCalendar.adapter = todayNotesAdapter
    }
}
