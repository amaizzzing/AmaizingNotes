package com.amaizzzing.amaizingnotes.model.interactors

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.User
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.channels.ReceiveChannel

interface TodayNotesInteractor {
    suspend fun insertNote(apiNote: ApiNote)

    suspend fun updateNote(apiNote: ApiNote)

    suspend fun deleteNote(apiNote: ApiNote)

    suspend fun deleteNoteById(id1: Long)

    fun getTodayNotes(startDay:Long,endDay:Long, typeRecord:String) : ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>>

    fun getAllNotes(startDay: Long, endDay: Long): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>>

    fun getCountFinishTasks(startDay: Long, endDay: Long) : Int

    fun getNoteById(id1:Long) : Maybe<ApiNote>?

    fun getNotFinishNotes() : Flowable<MutableList<Note>>?

    fun searchNotes(searchText:String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>>

    fun getCoefRatingForDays() : Float

    fun getCurrentUser():LiveData<User?>
}