package com.amaizzzing.amaizingnotes.model.data

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import io.reactivex.Flowable
import io.reactivex.Maybe

class TodayNoteDatasourceImpl : TodayNoteDatasource {
    override fun insertNote(apiNote: ApiNote): Maybe<Long>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.insertNote(apiNote)

    override fun updateNote(apiNote: ApiNote): Maybe<Int>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.updateNote(apiNote)

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.deleteNote(apiNote)

    override fun deleteNoteById(id1: Long): Maybe<Int>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.deleteNoteById(id1)

    override fun getTodayNote(startDay: Long, endDay: Long): Flowable<List<ApiNote>>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.getTodayNotes(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.getNoteById(id1)

    override fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>? =
        NotesApplication.instance.getAppDataBase()?.noteDao()?.getNotFinishNotes()
}