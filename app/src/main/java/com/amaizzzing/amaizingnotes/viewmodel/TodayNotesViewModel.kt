package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.*
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper

class TodayNotesViewModel() : ViewModel(), LifecycleObserver {
    var listTodayNotes: LiveData<MutableList<Note>>? = null
    private val interactor = NotesApplication.instance.getMyTodayNoteInteractor()

    /*init {
        getDataFromDB()
    }*/

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        //compositeDisposable.dispose()
    }

    fun fetchAllData(): LiveData<MutableList<Note>>? = listTodayNotes

    /*fun getDataFromDB() {
        listTodayNotes = interactor.getTodayNotes(DateFunctions.getStartDay(
            Calendar.getInstance().time.time),DateFunctions.getEndDay(
            Calendar.getInstance().time.time))?.let {
            Transformations.eosmap(
                it,
                { NoteMapper.listApiNoteToListNote(it) }
            )
        }
    }*/

    fun updateNote(note:Note){
        interactor.updateNote(NoteMapper.noteToApiNote(note))
    }

}