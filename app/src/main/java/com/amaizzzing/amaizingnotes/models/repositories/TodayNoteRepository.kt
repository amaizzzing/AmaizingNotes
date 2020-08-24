package com.amaizzzing.amaizingnotes.models.repositories

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.models.entities.Note
import io.reactivex.Observable

interface TodayNoteRepository{
    fun getTodayNote(startDay:Long,endDay:Long) : /*Observable<List<Note>>*/LiveData<MutableList<ApiNote>>?

    fun getNoteById(id1:Long) : ApiNote?

}