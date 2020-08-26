package com.amaizzzing.amaizingnotes.models.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ApiNote): Long

    @Update
    fun updateNote(note: ApiNote)

    @Delete
    fun deleteNote(note: ApiNote)

    @Query("select * from api_note where date between :startDay and :endDay order by isDone,date desc")
    fun getTodayNotes(startDay: Long, endDay: Long): LiveData<MutableList<ApiNote>>

    @Query("select * from api_note where id=:id1")
    fun getNoteById(id1: Long): ApiNote?

    @Query("select * from api_note where isDone=0 order by date desc")
    fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>
}