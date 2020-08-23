package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.*
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.utils.DateFunctions
import java.util.*

class TodayNotesViewModel() : ViewModel(), LifecycleObserver {
    var listTodayNotes: LiveData<MutableList<Note>>? = null

    init {
        getDataFromDB()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        //compositeDisposable.dispose()
    }

    fun fetchAllData(): LiveData<MutableList<Note>>? = listTodayNotes

    fun getDataFromDB() {
        listTodayNotes = NotesApplication.instance.getMyTodayNoteInteractor().getTodayNotes(DateFunctions.getStartDay(
            Calendar.getInstance().time.time),DateFunctions.getEndDay(
            Calendar.getInstance().time.time))?.let {
            Transformations.map(
                it,
                { NoteMapper.listApiNoteToListNote(it) }
            )
        }
    }

}