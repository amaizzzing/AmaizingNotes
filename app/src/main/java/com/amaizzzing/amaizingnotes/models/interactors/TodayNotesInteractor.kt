package com.amaizzzing.amaizingnotes.models.interactors

import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository
import io.reactivex.Observable

interface TodayNotesInteractor {
    fun getTodayNotes() : Observable<List<Note>>
}