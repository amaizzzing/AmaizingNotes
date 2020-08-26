package com.amaizzzing.amaizingnotes.models.interactors

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository

class TodayNotesInteractorImpl(val todayNoteRepository: TodayNoteRepository) :
    TodayNotesInteractor {
    override fun getTodayNotes(
        startDay: Long,
        endDay: Long
    ):/*Observable<List<Note>>*/LiveData<MutableList<ApiNote>>? =
        todayNoteRepository.getTodayNote(startDay, endDay)

    override fun getNoteById(id1: Long): ApiNote? = todayNoteRepository.getNoteById(id1)

    override fun updateNote(apiNote: ApiNote) {
        todayNoteRepository.updateNote(apiNote)
    }

    override fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>? =
        todayNoteRepository.getNotFinishNotes()
}