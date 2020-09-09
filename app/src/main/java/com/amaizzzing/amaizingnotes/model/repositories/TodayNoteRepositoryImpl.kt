package com.amaizzzing.amaizingnotes.model.repositories

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import io.reactivex.Flowable
import io.reactivex.Maybe

class TodayNoteRepositoryImpl(private val todayNoteDatasource: TodayNoteDatasource) :
    TodayNoteRepository {
    override fun insertNote(apiNote: ApiNote): Maybe<Long>? =
        todayNoteDatasource.insertNote(apiNote)

    override fun deleteNoteById(id1: Long): Maybe<Int>? =
        todayNoteDatasource.deleteNoteById(id1)

    override fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): Flowable<List<ApiNote>>? =
        todayNoteDatasource.getTodayNote(startDay, endDay, typeRecord)

    override fun getAllNotes(startDay: Long, endDay: Long): Flowable<List<ApiNote>> =
        todayNoteDatasource.getAllNotes(startDay, endDay)

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int =
        todayNoteDatasource.getCountFinishTasks(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        todayNoteDatasource.getNoteById(id1)

    override fun updateNote(apiNote: ApiNote): Maybe<Int>? =
        todayNoteDatasource.updateNote(apiNote)

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? =
        todayNoteDatasource.deleteNote(apiNote)

    override fun getNotFinishNotes(): Flowable<List<ApiNote>>? =
        todayNoteDatasource.getNotFinishNotes()

    override fun searchNotes(searchText: String): Flowable<List<ApiNote>> =
        todayNoteDatasource.searchNotes(searchText)

    override fun getCoefRatingForDays(): Double =
        todayNoteDatasource.getCoefRatingForDays()
}