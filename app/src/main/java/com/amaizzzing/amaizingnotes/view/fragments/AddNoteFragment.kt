package com.amaizzzing.amaizingnotes.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
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
import com.amaizzzing.amaizingnotes.utils.TIME_PATTERN
import com.amaizzzing.amaizingnotes.view.base.BaseViewState
import com.amaizzzing.amaizingnotes.viewmodel.AddNoteViewModel
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.add_note_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddNoteFragment : Fragment() {
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
    private lateinit var imgChooseBackAddNoteFragment:ImageView

    private var calendarDateTime = Calendar.getInstance()

    private var year = calendarDateTime.get(Calendar.YEAR)
    private var month = calendarDateTime.get(Calendar.MONTH)
    private var day = calendarDateTime.get(Calendar.DAY_OF_MONTH)
    private var hour = calendarDateTime.get(Calendar.HOUR)
    private var minutes = calendarDateTime.get(Calendar.MINUTE)

    private var idFromHomeFragment: Long? = -1L

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: AddNoteViewModel

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_note_fragment, container, false)

        val comp2 = DaggerComponent2.builder()
            .clearModule(ClearModule())
            .build()
        comp2.injectToAddNoteFragment(this)
        viewModel = ViewModelProvider(this, factory)[AddNoteViewModel::class.java]

        initViews(root)
        initListeners()

        idFromHomeFragment = arguments?.getLong(getString(R.string.current_note))

        return root
    }

    private fun renderUI(addNoteViewState: BaseViewState<Note>) {
        renderError(addNoteViewState.error)
        renderProgress()
        renderNote(addNoteViewState.data)
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
        compositeDisposable.add(
            viewModel.getChosenNote(idFromHomeFragment!!)
                ?.subscribe(
                    { renderUI(it) },
                    { renderUI(BaseViewState(false, Throwable(), null)) },
                    { renderUI(BaseViewState(false, null, null)) })!!
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
    }

    private fun initViews(v: View) {
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

    private fun initListeners() {
        imgChooseBackAddNoteFragment.setOnClickListener {
            val bottomDialog: BottomDialog =
                BottomDialog().newInstance()
            parentFragmentManager.let { bottomDialog.show(it,"o") }
        }

        btnOkAddNoteFragment.setOnClickListener {
            viewModel.insertNote(
                ApiNote(
                    id = idFromHomeFragment!!,
                    typeNote = if (radbutNoteAddNoteFragment.isChecked) NoteType.NOTE.type else NoteType.TASK.type,
                    date = calendarDateTime.time.time,
                    nameNote = etNameNoteAddNoteFragment.text.toString(),
                    text = etTextNoteAddNoteFragment.text.toString()
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

}