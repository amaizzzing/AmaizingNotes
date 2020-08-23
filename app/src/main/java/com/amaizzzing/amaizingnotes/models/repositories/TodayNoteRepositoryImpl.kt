package com.amaizzzing.amaizingnotes.models.repositories

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.models.entities.Note
import io.reactivex.Observable

class TodayNoteRepositoryImpl(val todayNoteDatasource: TodayNoteDatasource) : TodayNoteRepository {
    override fun getTodayNote(startDay:Long,endDay:Long): LiveData<MutableList<ApiNote>>? =
        todayNoteDatasource.getTodayNote(startDay, endDay)
    override fun getNoteById(id1: Long): ApiNote? =
        todayNoteDatasource.getNoteById(id1)
}