package com.amaizzzing.amaizingnotes.models.repositories

import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.models.entities.Note
import io.reactivex.Observable

class TodayNoteRepositoryImpl(val todayNoteDatasource: TodayNoteDatasource) : TodayNoteRepository {
    override fun getTodayNote(): Observable<List<Note>> = todayNoteDatasource.getTodayNote()
}