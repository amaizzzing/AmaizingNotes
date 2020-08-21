package com.amaizzzing.amaizingnotes.models.interactors

import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayNotesInteractorImpl(val todayNoteRepository: TodayNoteRepository) : TodayNotesInteractor {
    override fun getTodayNotes():Observable<List<Note>> = todayNoteRepository.getTodayNote()
}