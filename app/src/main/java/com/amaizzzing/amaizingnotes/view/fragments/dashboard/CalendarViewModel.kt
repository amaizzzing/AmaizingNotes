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
    var someDaysLiveData:MutableLiveData<MutableList<Note>>? = null


    /*fun getMyRange7Days() {
        someDaysLiveData=getSomeDays(Calendar.getInstance().time.time, Calendar.getInstance().time.time + 670211000L)

    }*/


    fun getMyRangeDays(range:Long) : LiveData<MutableList<Note>>? = getSomeDays(Calendar.getInstance().time.time,Calendar.getInstance().time.time + range)
        /*val mln = getSomeDays(
            Calendar.getInstance().time.time,
            Calendar.getInstance().time.time + 1340422000L
        )?.value
        someDaysLiveData?.value = mln*/


    /*fun getMyRange30Days(){
        someDaysLiveData = getSomeDays(
            Calendar.getInstance().time.time,
            Calendar.getInstance().time.time + 2872332857L
        )
    }*/

    fun getSomeDays(start:Long,end:Long) : LiveData<MutableList<Note>>? =
        NotesApplication.instance.getMyTodayNoteInteractor().getTodayNotes(
            DateFunctions.getStartDay(start),
            DateFunctions.getEndDay(end)
        )?.let {
            Transformations.map(
                it,
                { NoteMapper.listApiNoteToListNote(it) }
            )
        }
}