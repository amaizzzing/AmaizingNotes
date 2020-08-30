package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddNoteViewModel : ViewModel(), LifecycleObserver {
    fun insertNote(apiNote: ApiNote) {
        if (apiNote.id == -1L) {
            apiNote.id = 0L
            NotesApplication.instance.getMyTodayNoteInteractor().insertNote(apiNote)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe()
        } else {
            NotesApplication.instance.getMyTodayNoteInteractor().updateNote(apiNote)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe()
        }
    }

    fun deleteNoteById(id1: Long) {
        NotesApplication.instance.getMyTodayNoteInteractor().deleteNoteById(id1)
            ?.subscribeOn(Schedulers.io())
            ?.subscribe()
    }

    fun getChosenNote(id: Long) =
        NotesApplication.instance.getMyTodayNoteInteractor().getNoteById(id)
            ?.map { NoteMapper.apiNoteToNote(it) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    /*fun getNoteFromDB(id: Long) =
        NotesApplication.instance.getMyTodayNoteInteractor().getNoteById(id)*/

}