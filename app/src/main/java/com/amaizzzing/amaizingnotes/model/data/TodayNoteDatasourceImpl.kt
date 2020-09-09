package com.amaizzzing.amaizingnotes.model.data

import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.db.FirebaseDao
import com.amaizzzing.amaizingnotes.model.db.FirebaseDaoImpl
import com.amaizzzing.amaizingnotes.model.entities.Note
import io.reactivex.Flowable
import io.reactivex.Maybe

class TodayNoteDatasourceImpl : TodayNoteDatasource {

    val dataSource = NotesApplication.instance.fireBaseDao

    override fun insertNote(apiNote: ApiNote): Maybe<Long>? {
        //NotesApplication.instance.getAppDataBase().noteDao().insertNote(apiNote)
        return dataSource.insertNote(apiNote)
    }

    override fun updateNote(apiNote: ApiNote): Maybe<Int>? =
        NotesApplication.instance.getAppDataBase().noteDao().updateNote(apiNote)

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? =
        NotesApplication.instance.getAppDataBase().noteDao().deleteNote(apiNote)

    override fun deleteNoteById(id1: Long): Maybe<Int>? =
        NotesApplication.instance.getAppDataBase().noteDao().deleteNoteById(id1)

    override fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): Flowable<List<ApiNote>>? =
        NotesApplication.instance.getAppDataBase().noteDao().getTodayNotes(startDay, endDay, typeRecord)

    override fun getAllNotes(startDay: Long, endDay: Long): Flowable<List<ApiNote>> {
        NotesApplication.instance.getAppDataBase().noteDao().getAllNotes(startDay, endDay)
    return dataSource.getAllNotes()}

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int =
        NotesApplication.instance.getAppDataBase().noteDao().getCountFinishTasks(startDay, endDay)

    override fun getNoteById(id1: Long): Maybe<ApiNote>? =
        NotesApplication.instance.getAppDataBase().noteDao().getNoteById(id1)

    override fun getNotFinishNotes(): Flowable<List<ApiNote>>? =
        NotesApplication.instance.getAppDataBase().noteDao().getNotFinishNotes()

    override fun searchNotes(searchText: String): Flowable<List<ApiNote>> =
        NotesApplication.instance.getAppDataBase().noteDao().searchNotes(searchText)

    override fun getCoefRatingForDays(): Double =
        NotesApplication.instance.getAppDataBase().noteDao().getCoefRatingForDays()
}