package com.amaizzzing.amaizingnotes.model.repositories

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.User
import io.reactivex.Flowable
import io.reactivex.Maybe

interface TodayNoteRepository {
    fun insertNote(apiNote: ApiNote): Maybe<Long>?

    fun updateNote(apiNote: ApiNote): Maybe<Int>?

    fun deleteNote(apiNote: ApiNote): Maybe<Int>?

    fun deleteNoteById(id1: Long): Maybe<Int>?

    fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): Flowable<List<ApiNote>>?

    fun getAllNotes(startDay: Long, endDay: Long): Flowable<List<ApiNote>>

    fun getCountFinishTasks(startDay: Long, endDay: Long): Int

    fun getNoteById(id1: Long): Maybe<ApiNote>?

    fun getNotFinishNotes(): Flowable<List<ApiNote>>?

    fun searchNotes(searchText:String): Flowable<List<ApiNote>>

    fun getCoefRatingForDays(): Double

    fun getCurrentUser():LiveData<User?>
}