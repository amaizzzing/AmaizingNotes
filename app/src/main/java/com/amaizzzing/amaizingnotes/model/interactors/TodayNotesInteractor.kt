package com.amaizzzing.amaizingnotes.model.interactors

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import io.reactivex.Flowable
import io.reactivex.Maybe

interface TodayNotesInteractor {
    fun insertNote(apiNote: ApiNote): Maybe<Long>?

    fun updateNote(apiNote: ApiNote): Maybe<Int>?

    fun deleteNote(apiNote: ApiNote): Maybe<Int>?

    fun deleteNoteById(id1: Long) : Maybe<Int>?

    fun getTodayNotes(startDay:Long,endDay:Long, typeRecord:String) : Flowable<MutableList<Note>>?

    fun getAllNotes(startDay: Long, endDay: Long): Flowable<MutableList<Note>>

    fun getCountFinishTasks(startDay: Long, endDay: Long) : Int

    fun getNoteById(id1:Long) : Maybe<ApiNote>?

    fun getNotFinishNotes() : Flowable<MutableList<Note>>?

    fun searchNotes(searchText:String): Flowable<MutableList<Note>>

    fun getCoefRatingForDays() : Float
}