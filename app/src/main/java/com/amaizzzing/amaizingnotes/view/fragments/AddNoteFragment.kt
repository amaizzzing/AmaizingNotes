package com.amaizzzing.amaizingnotes.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amaizzzing.amaizingnotes.BottomDialog
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.utils.DATE_PATTERN
import com.amaizzzing.amaizingnotes.utils.DATE_TYPE
import com.amaizzzing.amaizingnotes.utils.TIME_PATTERN
import com.amaizzzing.amaizingnotes.view.base.BaseFragment
import com.amaizzzing.amaizingnotes.view.view_states.AddNoteViewState
import com.amaizzzing.amaizingnotes.viewmodel.AddNoteViewModel
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_note_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class AddNoteFragment : BaseFragment<Note, AddNoteViewState<Note>>() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    override val viewModel: AddNoteViewModel by lazy {
        ViewModelProvider(this, factory).get(AddNoteViewModel::class.java)
    }
    override val layoutRes: Int = R.layout.add_note_fragment
    override val rootView: View by lazy {
        this.layoutInflater.inflate(R.layout.add_note_fragment, container, false)
    }

    private lateinit var btnOkAddNoteFragment: ImageView
    private lateinit var backAddNoteFragment: ImageView
    private lateinit var btnDeleteAddNoteFragment: ImageView
    private lateinit var timeTextViewAddNoteFragment: TextView
    private lateinit var dateTextViewAddNoteFragment: TextView
    private lateinit var etNameNoteAddNoteFragment: TextInputEditText
    private lateinit var etTextNoteAddNoteFragment: TextInputEditText
    private lateinit var llContentAddNoteFragment: LinearLayout
    private lateinit var pbAddNoteFragment: FrameLayout
    private lateinit var radbutNoteAddNoteFragment: RadioButton
    private lateinit var radbutTaskAddNoteFragment: RadioButton
    private lateinit var imgChooseBackAddNoteFragment: ImageView

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
        val comp2 = DaggerComponent2.builder()
            .clearModule(ClearModule())
            .build()
        comp2.injectToAddNoteFragment(this)
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
        llContentAddNoteFragment.visibility = View.VISIBLE
        pbAddNoteFragment.visibility = View.GONE
        if (note != null) {
            fillViewsFromDB(note)
        } else {
            fillDefaultDateAndTime()
        }
    }

    private fun renderProgress() {
        llContentAddNoteFragment.visibility = View.GONE
        pbAddNoteFragment.visibility = View.VISIBLE
    }

    private fun renderError(error: Throwable?) {
        if (error != null) {
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
        dateTextViewAddNoteFragment.text =
            SimpleDateFormat(DATE_PATTERN, Locale.ROOT).format(calendarDateTime.time.time)
        timeTextViewAddNoteFragment.text =
            SimpleDateFormat(TIME_PATTERN, Locale.ROOT).format(calendarDateTime.time.time)
    }

    private fun fillViewsFromDB(noteFromDb: Note) {
        dateTextViewAddNoteFragment.text = noteFromDb.dateFormatted.split(" ")[0]
        timeTextViewAddNoteFragment.text = noteFromDb.dateFormatted.split(" ")[1]
        etNameNoteAddNoteFragment.setText(noteFromDb.nameNote)
        etTextNoteAddNoteFragment.setText(noteFromDb.text)
        if (noteFromDb.typeNote == NoteType.NOTE.type) {
            radbutNoteAddNoteFragment.isChecked = true
            radbutTaskAddNoteFragment.isChecked = false
        } else {
            radbutNoteAddNoteFragment.isChecked = false
            radbutTaskAddNoteFragment.isChecked = true
        }
        setBackgroundNote(noteFromDb.background)
    }

    override fun initViews(v: View) {
        btnOkAddNoteFragment = v.btn_ok_add_note_fragment
        btnDeleteAddNoteFragment = v.btn_delete_add_note_fragment
        backAddNoteFragment = v.back_add_note_fragment
        timeTextViewAddNoteFragment = v.time_text_view_add_note_fragment
        dateTextViewAddNoteFragment = v.date_text_view_add_note_fragment
        etNameNoteAddNoteFragment = v.et_name_note_add_note_fragment
        etTextNoteAddNoteFragment = v.et_text_note_add_note_fragment
        llContentAddNoteFragment = v.ll_content_add_note_fragment
        pbAddNoteFragment = v.pb_add_note_fragment
        radbutNoteAddNoteFragment = v.radbut_note_add_note_fragment
        radbutTaskAddNoteFragment = v.radbut_task_add_note_fragment
        imgChooseBackAddNoteFragment = v.img_choose_back_add_note_fragment
    }

    override fun initListeners() {
        imgChooseBackAddNoteFragment.setOnClickListener {
            val bottomDialog: BottomDialog = BottomDialog().newInstance()
            bottomDialog.setTargetFragment(this, 1)
            parentFragmentManager.let { bottomDialog.show(it, "bottomDialog") }
        }

        btnOkAddNoteFragment.setOnClickListener {
            viewModel.insertNote(
                ApiNote(
                    id = idFromHomeFragment!!,
                    typeNote = if (radbutNoteAddNoteFragment.isChecked) NoteType.NOTE.type else NoteType.TASK.type,
                    date = calendarDateTime.time.time,
                    nameNote = etNameNoteAddNoteFragment.text.toString(),
                    text = etTextNoteAddNoteFragment.text.toString(),
                    background = noteBackground
                )
            )
            findNavController().popBackStack()
        }

        btnDeleteAddNoteFragment.setOnClickListener {
            viewModel.deleteNoteById(idFromHomeFragment!!)
            findNavController().popBackStack()
        }

        dateTextViewAddNoteFragment.setOnClickListener {
            val dpd =
                context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        { _, year, monthOfYear, dayOfMonth ->
                            run {
                                calendarDateTime.set(year, monthOfYear, dayOfMonth)
                                dateTextViewAddNoteFragment.text =
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

        timeTextViewAddNoteFragment.setOnClickListener {
            val tpd = context?.let { it1 ->
                TimePickerDialog(
                    it1,
                    { _, hourOfDay, minute ->
                        run {
                            calendarDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendarDateTime.set(Calendar.MINUTE, minute)
                            timeTextViewAddNoteFragment.text =
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

        backAddNoteFragment.setOnClickListener {
            findNavController().popBackStack()
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
            0 -> llContentAddNoteFragment.setBackgroundColor(Color.WHITE)
            else -> {
                val dr = resources.getDrawable(resId)
                llContentAddNoteFragment.background = dr
            }
        }
    }
}