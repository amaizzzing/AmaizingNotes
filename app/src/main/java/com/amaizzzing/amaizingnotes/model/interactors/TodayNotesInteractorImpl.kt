package com.amaizzzing.amaizingnotes.model.interactors

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.User
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.ReceiveChannel

class TodayNotesInteractorImpl(private val todayNoteRepository: TodayNoteRepository) :
    TodayNotesInteractor {

    override suspend fun updateNote(apiNote: ApiNote) =
        todayNoteRepository.updateNote(apiNote)

    override suspend fun deleteNote(apiNote: ApiNote) =
        todayNoteRepository.deleteNote(apiNote)

    override suspend fun insertNote(apiNote: ApiNote) =
        todayNoteRepository.insertNote(apiNote)

    override suspend fun deleteNoteById(id1: Long) =
        todayNoteRepository.deleteNoteById(id1)

    override fun getTodayNotes(
        startDay: Long,
        endDay: Long,
        typeRecord: String
    ): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        todayNoteRepository.getTodayNote(startDay, endDay, typeRecord)
            /*?.map { it -> NoteMapper.listApiNoteToListNote(it) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())*/

    override fun getAllNotes(startDay: Long, endDay: Long): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        todayNoteRepository.getAllNotes(startDay, endDay)
            /*.map { NoteMapper.listApiNoteToListNote(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())*/

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int =
        todayNoteRepository.getCountFinishTasks(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        todayNoteRepository.getNoteById(id1)

    override fun getNotFinishNotes(): Flowable<MutableList<Note>>? =
        todayNoteRepository.getNotFinishNotes()
            ?.map { NoteMapper.listApiNoteToListNote(it) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    override fun searchNotes(searchText: String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        todayNoteRepository.searchNotes(searchText)
            /*.map { it -> NoteMapper.listApiNoteToListNote(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())*/

    override fun getCoefRatingForDays(): Float {
        val coef = todayNoteRepository.getCoefRatingForDays()
        return when {
            coef < 0 -> 3f
            coef in 0f..0.9f -> 2f
            else -> 1f
        }
    }

    override fun getCurrentUser(): LiveData<User?> =
        todayNoteRepository.getCurrentUser()
}