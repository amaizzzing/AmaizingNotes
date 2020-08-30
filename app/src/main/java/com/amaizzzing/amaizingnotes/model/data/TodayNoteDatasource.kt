package com.amaizzzing.amaizingnotes.model.data

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import io.reactivex.Flowable
import io.reactivex.Maybe

interface TodayNoteDatasource {
    fun insertNote(apiNote: ApiNote): Maybe<Long>?

    fun updateNote(apiNote: ApiNote): Maybe<Int>?

    fun deleteNote(apiNote: ApiNote): Maybe<Int>?

    fun deleteNoteById(id1: Long): Maybe<Int>?

    fun getTodayNote(startDay: Long, endDay: Long): Flowable<List<ApiNote>>?

    fun getNoteById(id1: Long): Maybe<ApiNote>?

    fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>?
}