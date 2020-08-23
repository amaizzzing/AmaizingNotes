package com.amaizzzing.amaizingnotes.view.fragments.add_note

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddNoteFragment : Fragment() {
    lateinit var btnOkAddNoteFragment: Button
    lateinit var btnCancelAddNoteFragment: Button
    lateinit var timeTextViewAddNoteFragment: TextView
    lateinit var dateTextViewAddNoteFragment: TextView
    lateinit var etNameNoteAddNoteFragment: TextInputEditText
    lateinit var etTextNoteAddNoteFragment: TextInputEditText

    var calendarDateTime = Calendar.getInstance()

    var year = calendarDateTime.get(Calendar.YEAR)
    var month = calendarDateTime.get(Calendar.MONTH)
    var day = calendarDateTime.get(Calendar.DAY_OF_MONTH)
    var hour = calendarDateTime.get(Calendar.HOUR)
    var minutes = calendarDateTime.get(Calendar.MINUTE)
    var seconds = calendarDateTime.get(Calendar.SECOND)

    var idFromHomeFragment : Long? =-1L

    companion object {
        fun newInstance() = AddNoteFragment()
    }

    private lateinit var viewModel: AddNoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_note_fragment, container, false)

        viewModel = ViewModelProvider(this)[AddNoteViewModel::class.java]


        initViews(root)
        initListeners()

        idFromHomeFragment  = arguments?.getLong(getString(R.string.current_note))
        if (idFromHomeFragment == -1L ) {
            dateTextViewAddNoteFragment.text =
                SimpleDateFormat("dd.MM.yyyy").format(calendarDateTime.time.time)
            timeTextViewAddNoteFragment.text =
                SimpleDateFormat("HH:mm").format(calendarDateTime.time.time)
        } else {
            with(CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
                launch {
                    withContext(Dispatchers.IO){
                        val noteFromDb =  viewModel.getChosenNote(idFromHomeFragment!!)
                        withContext(Dispatchers.Main){
                            dateTextViewAddNoteFragment.text = noteFromDb.date.split(" ")[0]
                            timeTextViewAddNoteFragment.text = noteFromDb.date.split(" ")[1]
                            etNameNoteAddNoteFragment.setText(noteFromDb.nameNote)
                            etTextNoteAddNoteFragment.setText(noteFromDb.text)
                        }
                    }
                }
            }


            /*viewModel.getNoteFromDB(idFromHomeFragment!!)?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                dateTextViewAddNoteFragment.text = it.date.split(" ")[0]
                timeTextViewAddNoteFragment.text = it.date.split(" ")[1]
                etNameNoteAddNoteFragment.setText(it.nameNote)
                etTextNoteAddNoteFragment.setText(it.text)
            })*/
        }

        return root
    }

    fun initViews(v: View) {
        btnOkAddNoteFragment = v.findViewById(R.id.btn_ok_add_note_fragment)
        btnCancelAddNoteFragment = v.findViewById(R.id.btn_cancel_add_note_fragment)
        timeTextViewAddNoteFragment = v.findViewById(R.id.time_text_view_add_note_fragment)
        dateTextViewAddNoteFragment = v.findViewById(R.id.date_text_view_add_note_fragment)
        etNameNoteAddNoteFragment = v.findViewById(R.id.et_name_note_add_note_fragment)
        etTextNoteAddNoteFragment = v.findViewById(R.id.et_text_note_add_note_fragment)
    }

    fun initListeners() {
        btnOkAddNoteFragment.setOnClickListener {
            val noteDao = NotesApplication.instance.getAppNoteDao()
            val tmpApiNote=ApiNote(
                id = 0,
                date = calendarDateTime.time.time,
                nameNote = etNameNoteAddNoteFragment.text.toString(),
                text = etTextNoteAddNoteFragment.text.toString()
            )
            if(idFromHomeFragment == -1L){
                with(CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
                    launch {
                        noteDao?.insertNote(tmpApiNote)
                        findNavController().popBackStack()
                    }
                }
            }else{
                tmpApiNote.id = idFromHomeFragment!!
                with(CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
                    launch {
                        noteDao?.updateNode(tmpApiNote)
                        findNavController().popBackStack()
                    }
                }
            }
        }
        dateTextViewAddNoteFragment.setOnClickListener {
            val dpd =
                context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            run {
                                calendarDateTime.set(year, monthOfYear, dayOfMonth)
                                dateTextViewAddNoteFragment.text =
                                    SimpleDateFormat("dd.MM.yyyy").format(calendarDateTime.time.time)
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
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        run {
                            calendarDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendarDateTime.set(Calendar.MINUTE, minute)
                            timeTextViewAddNoteFragment.text =
                                SimpleDateFormat("HH:mm").format(calendarDateTime.time.time)
                        }
                    },
                    hour,
                    minutes,
                    true
                )
            }
            tpd?.show()
        }
        btnCancelAddNoteFragment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddNoteViewModel::class.java)
        // TODO: Use the ViewModel
    }

}