package com.amaizzzing.amaizingnotes.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.utils.DATE_PATTERN
import com.amaizzzing.amaizingnotes.utils.DATE_TYPE
import com.amaizzzing.amaizingnotes.utils.TIME_PATTERN
import com.amaizzzing.amaizingnotes.view.base.BaseFragment
import com.amaizzzing.amaizingnotes.view.dialogs.BottomDialog
import com.amaizzzing.amaizingnotes.view.view_states.AddNoteViewState
import com.amaizzzing.amaizingnotes.viewmodel.AddNoteViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note_fragment.view.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class AddNoteFragment : BaseFragment<AddNoteViewState<Note>>() {
    override val viewModel: AddNoteViewModel by viewModel()
    val navController by lazy {
        findNavController()
    }
    override val layoutRes: Int = R.layout.add_note_fragment
    override val rootView: View by lazy {
        this.layoutInflater.inflate(R.layout.add_note_fragment, container, false)
    }

    private var calendarDateTime = Calendar.getInstance()

    private var year = calendarDateTime.get(Calendar.YEAR)
    private var month = calendarDateTime.get(Calendar.MONTH)
    private var day = calendarDateTime.get(Calendar.DAY_OF_MONTH)
    private var hour = calendarDateTime.get(Calendar.HOUR)
    private var minutes = calendarDateTime.get(Calendar.MINUTE)

    private var idFromHomeFragment: Long? = -1L

    private var noteBackground = 0

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        idFromHomeFragment = arguments?.getLong(getString(R.string.current_note))

        return rootView
    }

    override fun renderUI(data: AddNoteViewState<Note>) {
        renderError(data.error)
        renderProgress()
        renderNote(data.data)
    }

    private fun renderNote(note: Note?) {
        rootView.ll_content_add_note_fragment.visibility = View.VISIBLE
        rootView.pb_add_note_fragment.visibility = View.GONE
        note?.let { fillViewsFromDB(note) } ?: fillDefaultDateAndTime()
    }

    private fun renderProgress() {
        rootView.ll_content_add_note_fragment.visibility = View.GONE
        rootView.pb_add_note_fragment.visibility = View.VISIBLE
    }

    private fun renderError(error: Throwable?) {
        error?.let {
            Toast.makeText(context, "${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        fillDefaultDateAndTime()
        compositeDisposable.add(
            viewModel.getChosenNote(idFromHomeFragment!!)
                ?.subscribe { renderUI(it) }!!
        )
    }

    private fun fillDefaultDateAndTime() {
        rootView.date_text_view_add_note_fragment.text =
            SimpleDateFormat(DATE_PATTERN, Locale.ROOT).format(calendarDateTime.time.time)
        rootView.time_text_view_add_note_fragment.text =
            SimpleDateFormat(TIME_PATTERN, Locale.ROOT).format(calendarDateTime.time.time)
    }

    private fun fillViewsFromDB(noteFromDb: Note) {
        rootView.date_text_view_add_note_fragment.text = noteFromDb.dateFormatted.split(" ")[0]
        rootView.time_text_view_add_note_fragment.text = noteFromDb.dateFormatted.split(" ")[1]
        rootView.et_name_note_add_note_fragment.setText(noteFromDb.nameNote)
        rootView.et_text_note_add_note_fragment.setText(noteFromDb.text)
        if (noteFromDb.typeNote == NoteType.NOTE.type) {
            rootView.radbut_note_add_note_fragment.isChecked = true
            rootView.radbut_task_add_note_fragment.isChecked = false
        } else {
            rootView.radbut_note_add_note_fragment.isChecked = false
            rootView.radbut_task_add_note_fragment.isChecked = true
        }
        setBackgroundNote(noteFromDb.background)
    }

    override fun initListeners() {
        rootView.img_choose_back_add_note_fragment.setOnClickListener {
            val bottomDialog: BottomDialog = BottomDialog().newInstance()
            bottomDialog.setTargetFragment(this, 1)
            parentFragmentManager.let { bottomDialog.show(it, "bottomDialog") }
        }

        rootView.btn_ok_add_note_fragment.setOnClickListener {
            viewModel.insertNote(
                ApiNote(
                    id = idFromHomeFragment!!,
                    typeNote = if (rootView.radbut_note_add_note_fragment.isChecked) NoteType.NOTE.type else NoteType.TASK.type,
                    date = calendarDateTime.time.time,
                    nameNote = rootView.et_name_note_add_note_fragment.text.toString(),
                    text = rootView.et_text_note_add_note_fragment.text.toString(),
                    background = noteBackground
                )
            )
            navController.popBackStack()
        }

        rootView.btn_delete_add_note_fragment.setOnClickListener {
            launch {
                viewModel.deleteNoteById(idFromHomeFragment!!)
            }
            navController.popBackStack()
        }

        rootView.date_text_view_add_note_fragment.setOnClickListener {
            val dpd =
                context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        { _, year, monthOfYear, dayOfMonth ->
                            run {
                                calendarDateTime.set(year, monthOfYear, dayOfMonth)
                                rootView.date_text_view_add_note_fragment.text =
                                    SimpleDateFormat(DATE_PATTERN, Locale.ROOT).format(
                                        calendarDateTime.time.time
                                    )
                            }
                        },
                        year,
                        month,
                        day
                    )
                }
            dpd?.show()
        }

        rootView.time_text_view_add_note_fragment.setOnClickListener {
            val tpd = context?.let { it1 ->
                TimePickerDialog(
                    it1,
                    { _, hourOfDay, minute ->
                        run {
                            calendarDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendarDateTime.set(Calendar.MINUTE, minute)
                            rootView.time_text_view_add_note_fragment.text =
                                SimpleDateFormat(
                                    TIME_PATTERN,
                                    Locale.ROOT
                                ).format(calendarDateTime.time.time)
                        }
                    },
                    hour,
                    minutes,
                    true
                )
            }
            tpd?.show()
        }

        rootView.back_add_note_fragment.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == 1) {
            val resId = data?.getIntExtra(DATE_TYPE, -1)
            resId?.let {
                setBackgroundNote(resId)
            }
        }
    }

    private fun setBackgroundNote(resId: Int) {
        noteBackground = resId
        when (resId) {
            0 -> rootView.ll_content_add_note_fragment.setBackgroundColor(Color.WHITE)
            else -> {
                val dr = resources.getDrawable(resId)
                rootView.ll_content_add_note_fragment.background = dr
            }
        }
    }
}