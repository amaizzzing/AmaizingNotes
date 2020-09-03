package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddNoteViewModel @Inject constructor(var interactor: TodayNotesInteractor) : ViewModel(),
    LifecycleObserver {
    fun insertNote(apiNote: ApiNote) {
        if (apiNote.id == -1L) {
            apiNote.id = 0L
            interactor.insertNote(apiNote)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe()
        } else {
            interactor.updateNote(apiNote)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe()
        }
    }

    fun deleteNoteById(id1: Long) {
        interactor.deleteNoteById(id1)
            ?.subscribeOn(Schedulers.io())
            ?.subscribe()
    }

    fun getChosenNote(id: Long) =
        interactor.getNoteById(id)
            ?.map { NoteMapper.apiNoteToNote(it) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
}