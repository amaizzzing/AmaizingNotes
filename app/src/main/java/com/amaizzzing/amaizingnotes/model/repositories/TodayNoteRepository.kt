package com.amaizzzing.amaizingnotes.model.repositories

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.User
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.channels.ReceiveChannel

interface TodayNoteRepository {
    suspend fun insertNote(apiNote: ApiNote)

    suspend fun updateNote(apiNote: ApiNote)

    suspend fun deleteNote(apiNote: ApiNote)

    suspend fun deleteNoteById(id1: Long)

    fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>>

    fun getAllNotes(startDay: Long, endDay: Long): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>>

    fun getCountFinishTasks(startDay: Long, endDay: Long): Int

    fun getNoteById(id1: Long): Maybe<ApiNote>?

    fun getNotFinishNotes(): Flowable<List<ApiNote>>?

    fun searchNotes(searchText:String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>>

    fun getCoefRatingForDays(): Double

    fun getCurrentUser():LiveData<User?>
}