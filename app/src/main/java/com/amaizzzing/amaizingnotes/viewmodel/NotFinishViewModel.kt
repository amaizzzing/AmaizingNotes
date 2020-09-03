package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NotFinishViewModel @Inject constructor(var interactor: TodayNotesInteractor): ViewModel() {
    var listNotFinishNotes: LiveData<MutableList<Note>>? = null

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

    fun updateNote(note: Note) {
        interactor.updateNote(NoteMapper.noteToApiNote(note))
            ?.subscribeOn(Schedulers.io())
            ?.subscribe()
    }
}