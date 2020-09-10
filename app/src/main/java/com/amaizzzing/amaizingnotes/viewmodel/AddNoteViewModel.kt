package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.view.base.BaseViewModel
import com.amaizzzing.amaizingnotes.view.base.BaseViewState
import com.amaizzzing.amaizingnotes.view.view_states.AddNoteViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

const val DEFAULT_ID = -1L

class AddNoteViewModel @Inject constructor(var interactor: TodayNotesInteractor) :
    BaseViewModel<Note,AddNoteViewState<Note>>() {
    fun insertNote(apiNote: ApiNote) {
        if (apiNote.id == DEFAULT_ID) {
            apiNote.id = apiNote.date
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
            ?.map{AddNoteViewState(false,null,it)}
            ?.onErrorReturn { AddNoteViewState(false,it,null) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
}