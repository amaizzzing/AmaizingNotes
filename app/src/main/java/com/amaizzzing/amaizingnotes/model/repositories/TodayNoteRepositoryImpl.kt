package com.amaizzzing.amaizingnotes.model.repositories

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.entities.User
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.channels.ReceiveChannel

class TodayNoteRepositoryImpl(private val todayNoteDatasource: TodayNoteDatasource) :
    TodayNoteRepository {
    override suspend fun insertNote(apiNote: ApiNote) =
        todayNoteDatasource.insertNote(apiNote)

    override suspend fun deleteNoteById(id1: Long) =
        todayNoteDatasource.deleteNoteById(id1)

    override fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        todayNoteDatasource.getTodayNote(startDay, endDay, typeRecord)

    override fun getAllNotes(startDay: Long, endDay: Long): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        todayNoteDatasource.getAllNotes(startDay, endDay)

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int =
        todayNoteDatasource.getCountFinishTasks(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        todayNoteDatasource.getNoteById(id1)

    override suspend fun updateNote(apiNote: ApiNote) =
        todayNoteDatasource.updateNote(apiNote)

    override suspend fun deleteNote(apiNote: ApiNote) =
        todayNoteDatasource.deleteNote(apiNote)

    override fun getNotFinishNotes(): Flowable<List<ApiNote>>? =
        todayNoteDatasource.getNotFinishNotes()

    override fun searchNotes(searchText: String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        todayNoteDatasource.searchNotes(searchText)

    override fun getCoefRatingForDays(): Double =
        todayNoteDatasource.getCoefRatingForDays()

    override fun getCurrentUser(): LiveData<User?> =
        todayNoteDatasource.getCurrentUser()
}