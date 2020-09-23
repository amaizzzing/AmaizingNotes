package com.amaizzzing.amaizingnotes.model.data

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.User
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.channels.ReceiveChannel

class TodayNoteDatasourceImpl(val dataSource : TodayNoteDatasource) : TodayNoteDatasource {
    override suspend fun insertNote(apiNote: ApiNote): Unit =
        dataSource.insertNote(apiNote)

    override suspend fun updateNote(apiNote: ApiNote)=
        dataSource.updateNote(apiNote)

    override suspend fun deleteNote(apiNote: ApiNote) =
        dataSource.deleteNote(apiNote)

    override suspend fun deleteNoteById(id1: Long) =
        dataSource.deleteNoteById(id1)

    override fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        dataSource.getTodayNote(startDay,endDay, typeRecord)

    override fun getAllNotes(startDay: Long, endDay: Long): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        dataSource.getAllNotes(startDay,endDay)

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int =
        dataSource.getCountFinishTasks(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        dataSource.getNoteById(id1)

    override fun getNotFinishNotes(): Flowable<List<ApiNote>>? =
        dataSource.getNotFinishNotes()

    override fun searchNotes(searchText: String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        dataSource.searchNotes(searchText)

    override fun getCoefRatingForDays(): Double =
        dataSource.getCoefRatingForDays()

    override fun getCurrentUser(): LiveData<User?> =
        dataSource.getCurrentUser()
}