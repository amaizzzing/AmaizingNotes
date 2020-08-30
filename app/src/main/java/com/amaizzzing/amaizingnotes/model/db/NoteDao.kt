package com.amaizzzing.amaizingnotes.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: ApiNote): Maybe<Long>

    @Update
    fun updateNote(note: ApiNote): Maybe<Int>

    @Delete
    fun deleteNote(note: ApiNote): Maybe<Int>

    @Query("delete from api_note where id=:id1")
    fun deleteNoteById(id1: Long): Maybe<Int>

    @Query("select * from api_note where date between :startDay and :endDay order by isDone,date desc")
    fun getTodayNotes(startDay: Long, endDay: Long): Flowable<List<ApiNote>>

    @Query("select * from api_note where id=:id1")
    fun getNoteById(id1: Long): Maybe<ApiNote>

    @Query("select * from api_note where isDone=0 order by date desc")
    fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>
}