package com.amaizzzing.amaizingnotes.model.interactors

import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayNotesInteractorImpl(private val todayNoteRepository: TodayNoteRepository) :
    TodayNotesInteractor {

    override fun updateNote(apiNote: ApiNote): Maybe<Int>? =
        todayNoteRepository.updateNote(apiNote)

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? =
        todayNoteRepository.deleteNote(apiNote)

    override fun insertNote(apiNote: ApiNote): Maybe<Long>? =
        todayNoteRepository.insertNote(apiNote)

    override fun deleteNoteById(id1: Long): Maybe<Int>? =
        todayNoteRepository.deleteNoteById(id1)

    override fun getTodayNotes(
        startDay: Long,
        endDay: Long,
        typeRecord: String
    ): Flowable<MutableList<Note>>? =
        todayNoteRepository.getTodayNote(startDay, endDay, typeRecord)
            ?.map { it -> NoteMapper.listApiNoteToListNote(it) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    override fun getAllNotes(startDay: Long, endDay: Long): Flowable<MutableList<Note>> =
        todayNoteRepository.getAllNotes(startDay, endDay)
            .map { NoteMapper.listApiNoteToListNote(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int =
        todayNoteRepository.getCountFinishTasks(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        todayNoteRepository.getNoteById(id1)

    override fun getNotFinishNotes(): Flowable<MutableList<Note>>? =
        todayNoteRepository.getNotFinishNotes()
            ?.map { NoteMapper.listApiNoteToListNote(it) }
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())

    override fun searchNotes(searchText: String): Flowable<MutableList<Note>> =
        todayNoteRepository.searchNotes(searchText)
            .map { it -> NoteMapper.listApiNoteToListNote(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCoefRatingForDays(): Float {
        val coef = todayNoteRepository.getCoefRatingForDays()
        return when {
            coef < 0 -> 3f
            coef in 0f..0.9f -> 2f
            else -> 1f
        }
    }
}