package com.amaizzzing.amaizingnotes.models.interactors

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository
import io.reactivex.Observable

interface TodayNotesInteractor {
    fun getTodayNotes(startDay:Long,endDay:Long) : /*Observable<List<Note>>*/LiveData<MutableList<ApiNote>>?

    fun getNoteById(id1:Long) : ApiNote?

    fun updateNote(apiNote:ApiNote)

    fun getNotFinishNotes() : LiveData<MutableList<ApiNote>>?
}