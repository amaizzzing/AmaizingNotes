package com.amaizzzing.amaizingnotes.view.fragments.add_note

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper

class AddNoteViewModel : ViewModel(), LifecycleObserver {

    fun getChosenNote(id: Long): Note = NoteMapper.apiNoteToNote(getNoteFromDB(id)!!)

    fun getNoteFromDB(id: Long) = NotesApplication
        .instance
        .getMyTodayNoteInteractor()
        .getNoteById(id)

}