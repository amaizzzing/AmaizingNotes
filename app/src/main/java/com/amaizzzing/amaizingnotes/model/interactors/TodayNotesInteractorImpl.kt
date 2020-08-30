package com.amaizzzing.amaizingnotes.model.interactors

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import io.reactivex.Flowable
import io.reactivex.Maybe

class TodayNotesInteractorImpl(private val todayNoteRepository: TodayNoteRepository) :
    TodayNotesInteractor {

    override fun updateNote(apiNote: ApiNote) : Maybe<Int>?=
        todayNoteRepository.updateNote(apiNote)

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? =
        todayNoteRepository.deleteNote(apiNote)

    override fun insertNote(apiNote: ApiNote): Maybe<Long>? =
        todayNoteRepository.insertNote(apiNote)

    override fun deleteNoteById(id1: Long): Maybe<Int>? =
        todayNoteRepository.deleteNoteById(id1)

    override fun getTodayNotes(startDay: Long, endDay: Long): Flowable<List<ApiNote>>? =
        todayNoteRepository.getTodayNote(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        todayNoteRepository.getNoteById(id1)

    override fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>? =
        todayNoteRepository.getNotFinishNotes()
}