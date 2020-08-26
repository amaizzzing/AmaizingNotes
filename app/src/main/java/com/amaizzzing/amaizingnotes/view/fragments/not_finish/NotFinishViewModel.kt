package com.amaizzzing.amaizingnotes.view.fragments.not_finish

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper

class NotFinishViewModel : ViewModel() {
    var listNotFinishNotes: LiveData<MutableList<Note>>? = null
    private val interactor = NotesApplication.instance.getMyTodayNoteInteractor()

    init {
        getNotFinishNotesFromDB()
    }

    fun fetchAllNotFinishNotes(): LiveData<MutableList<Note>>? = listNotFinishNotes

    fun getNotFinishNotesFromDB() {
        listNotFinishNotes = interactor.getNotFinishNotes()?.let {
            Transformations.map(
                it,
                { NoteMapper.listApiNoteToListNote(it) }
            )
        }
    }

    fun updateNote(note:Note){
        interactor.updateNote(NoteMapper.noteToApiNote(note))
    }
}