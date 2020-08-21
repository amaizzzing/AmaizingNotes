package com.amaizzzing.amaizingnotes.models.data

import com.amaizzzing.amaizingnotes.models.entities.Note
import io.reactivex.Observable

class TodayNoteDatasourceImpl : TodayNoteDatasource {
    override fun getTodayNote(): Observable<List<Note>> =
        Observable.fromArray(listOf(
        Note(0,111111,"olololo"),
        Note(1,111111,"olololo"),
        Note(2,111111,"olololo"),
        Note(3,111111,"olololo"),
        Note(4,111111,"olololo")
    ))

}