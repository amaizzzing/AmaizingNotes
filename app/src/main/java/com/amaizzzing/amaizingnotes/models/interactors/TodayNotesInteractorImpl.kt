package com.amaizzzing.amaizingnotes.models.interactors

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayNotesInteractorImpl(val todayNoteRepository: TodayNoteRepository) : TodayNotesInteractor {
    override fun getTodayNotes(startDay:Long,endDay:Long):/*Observable<List<Note>>*/LiveData<MutableList<ApiNote>>? =
        todayNoteRepository.getTodayNote(startDay,endDay)

    override fun getNoteById(id1: Long): ApiNote? = todayNoteRepository.getNoteById(id1)

}