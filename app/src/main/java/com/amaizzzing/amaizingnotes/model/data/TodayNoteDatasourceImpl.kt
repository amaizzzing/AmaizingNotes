package com.amaizzzing.amaizingnotes.model.data

import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import io.reactivex.Flowable
import io.reactivex.Maybe

class TodayNoteDatasourceImpl : TodayNoteDatasource {
    private val dataSource : TodayNoteDatasource = NotesApplication.instance.fireBaseDao

    override fun insertNote(apiNote: ApiNote): Maybe<Long>? =
        dataSource.insertNote(apiNote)

    override fun updateNote(apiNote: ApiNote): Maybe<Int>? =
        dataSource.updateNote(apiNote)

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? =
        dataSource.deleteNote(apiNote)

    override fun deleteNoteById(id1: Long): Maybe<Int>? =
        dataSource.deleteNoteById(id1)

    override fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): Flowable<List<ApiNote>>? =
        dataSource.getTodayNote(startDay,endDay, typeRecord)

    override fun getAllNotes(startDay: Long, endDay: Long): Flowable<List<ApiNote>> =
        dataSource.getAllNotes(startDay,endDay)

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int =
        dataSource.getCountFinishTasks(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        dataSource.getNoteById(id1)

    override fun getNotFinishNotes(): Flowable<List<ApiNote>>? =
        dataSource.getNotFinishNotes()

    override fun searchNotes(searchText: String): Flowable<List<ApiNote>> =
        dataSource.searchNotes(searchText)

    override fun getCoefRatingForDays(): Double =
        dataSource.getCoefRatingForDays()
}