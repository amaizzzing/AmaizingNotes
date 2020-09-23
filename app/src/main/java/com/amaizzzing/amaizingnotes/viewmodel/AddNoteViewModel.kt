package com.amaizzzing.amaizingnotes.viewmodel

import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.view.base.BaseViewModel
import com.amaizzzing.amaizingnotes.view.view_states.AddNoteViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

const val DEFAULT_ID = -1L

class AddNoteViewModel(val interactor: TodayNotesInteractor) :
    BaseViewModel<AddNoteViewState<Note>>() {
    fun insertNote(apiNote: ApiNote) {
        if (apiNote.id == DEFAULT_ID) {
            apiNote.id = apiNote.date
            launch {
                interactor.insertNote(apiNote)
            }

        } else {
            launch {
                interactor.updateNote(apiNote)
            }
        }
    }

    suspend fun deleteNoteById(id1: Long) {
        launch {
            interactor.deleteNoteById(id1)
        }

    }

    fun getChosenNote(id: Long) =
        interactor.getNoteById(id)
            ?.map { NoteMapper.apiNoteToNote(it) }
            ?.map { AddNoteViewState(false, null, it) }
            ?.onErrorReturn { AddNoteViewState(false, it, null) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
}