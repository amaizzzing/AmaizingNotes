package com.amaizzzing.amaizingnotes.model.repositories

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import io.reactivex.Flowable
import io.reactivex.Maybe

class TodayNoteRepositoryImpl(private val todayNoteDatasource: TodayNoteDatasource) : TodayNoteRepository {
    override fun insertNote(apiNote: ApiNote): Maybe<Long>? =
        todayNoteDatasource.insertNote(apiNote)

    override fun deleteNoteById(id1: Long): Maybe<Int>? =
        todayNoteDatasource.deleteNoteById(id1)

    override fun getTodayNote(startDay: Long, endDay: Long): Flowable<List<ApiNote>>? =
        todayNoteDatasource.getTodayNote(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        todayNoteDatasource.getNoteById(id1)

    override fun updateNote(apiNote: ApiNote) : Maybe<Int>? =
        todayNoteDatasource.updateNote(apiNote)

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? =
        todayNoteDatasource.deleteNote(apiNote)

    override fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>? =
        todayNoteDatasource.getNotFinishNotes()
}