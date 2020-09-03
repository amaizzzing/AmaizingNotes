package com.amaizzzing.amaizingnotes.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.utils.DATE_PATTERN
import com.amaizzzing.amaizingnotes.utils.TIME_PATTERN
import com.amaizzzing.amaizingnotes.viewmodel.AddNoteViewModel
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
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
        viewModel = ViewModelProvider(this,factory)[AddNoteViewModel::class.java]

        initViews(root)
        initListeners()

        idFromHomeFragment = arguments?.getLong(getString(R.string.current_note))
        initAndShowViews()

        return root
    }

    private fun initAndShowViews() {
        if (idFromHomeFragment == -1L) {
            fillDefaultDateAndTime()
        } else {
            fillWithNoteDateAndTime()
        }
    }

    private fun fillDefaultDateAndTime() {
        dateTextViewAddNoteFragment.text =
            SimpleDateFormat(DATE_PATTERN, Locale.ROOT).format(calendarDateTime.time.time)
        timeTextViewAddNoteFragment.text =
            SimpleDateFormat(TIME_PATTERN, Locale.ROOT).format(calendarDateTime.time.time)
    }

    private fun fillWithNoteDateAndTime() {
        compositeDisposable.add(
            viewModel.getChosenNote(idFromHomeFragment!!)
                ?.subscribeBy { note -> fillViewsFromDB(note) }!!
        )
    }

    private fun fillViewsFromDB(noteFromDb: Note) {
        dateTextViewAddNoteFragment.text = noteFromDb.date.split(" ")[0]
        timeTextViewAddNoteFragment.text = noteFromDb.date.split(" ")[1]
        etNameNoteAddNoteFragment.setText(noteFromDb.nameNote)
        etTextNoteAddNoteFragment.setText(noteFromDb.text)
    }

    private fun initViews(v: View) {
        btnOkAddNoteFragment = v.btn_ok_add_note_fragment
        btnDeleteAddNoteFragment = v.btn_delete_add_note_fragment
        backAddNoteFragment = v.back_add_note_fragment
        timeTextViewAddNoteFragment = v.time_text_view_add_note_fragment
        dateTextViewAddNoteFragment = v.date_text_view_add_note_fragment
        etNameNoteAddNoteFragment = v.et_name_note_add_note_fragment
        etTextNoteAddNoteFragment = v.et_text_note_add_note_fragment
    }

    private fun initListeners() {
        btnOkAddNoteFragment.setOnClickListener {
            viewModel.insertNote(
                ApiNote(
                    id = idFromHomeFragment!!,
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