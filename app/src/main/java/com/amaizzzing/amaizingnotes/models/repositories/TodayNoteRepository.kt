package com.amaizzzing.amaizingnotes.models.repositories

import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.models.entities.Note
import io.reactivex.Observable

interface TodayNoteRepository{
    fun getTodayNote() : Observable<List<Note>>
}