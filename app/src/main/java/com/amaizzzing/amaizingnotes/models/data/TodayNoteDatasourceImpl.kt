package com.amaizzzing.amaizingnotes.models.data

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote

class TodayNoteDatasourceImpl : TodayNoteDatasource {
    override fun getTodayNote(startDay:Long,endDay:Long): LiveData<MutableList<ApiNote>>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.getTodayNotes(startDay, endDay)/*Observable<List<Note>> =*/

    override fun getNoteById(id1: Long): ApiNote? = NotesApplication.instance.getAppDataBase()?.noteDao()?.getNoteById(id1)
    override fun updateNote(apiNote: ApiNote) {
        NotesApplication.instance.getAppDataBase()?.noteDao()?.updateNote(apiNote)
    }


}