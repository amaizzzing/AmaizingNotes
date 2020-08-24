package com.amaizzzing.amaizingnotes.view.fragments.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.utils.DateFunctions
import java.util.*

class CalendarViewModel : ViewModel() {
    fun getMyRange7Days() : LiveData<MutableList<Note>>? {
        Log.e("MyDB","${DateFunctions.getStartDay(Calendar.getInstance().time.time)} ${DateFunctions.getEndDay(Calendar.getInstance().time.time + 673199000L)}")
        return NotesApplication.instance.getMyTodayNoteInteractor().getTodayNotes(
            DateFunctions.getStartDay(
                Calendar.getInstance().time.time
            ),
            DateFunctions.getEndDay(
                Calendar.getInstance().time.time + 673199000L
            )
        )?.let {
            Transformations.map(
                it,
                { NoteMapper.listApiNoteToListNote(it) }
            )
        }
    }

    /*fun getMyRange14Days() : LiveData<MutableList<Note>>? = range14Days

    fun getMyRange30Days(): LiveData<MutableList<Note>>? = range30days*/
}