package com.amaizzzing.amaizingnotes.models.data

import com.amaizzzing.amaizingnotes.models.entities.Note
import io.reactivex.Observable

interface TodayNoteDatasource {
    fun getTodayNote() :Observable<List<Note>>
}