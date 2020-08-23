package com.amaizzzing.amaizingnotes.models.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote
import com.amaizzzing.amaizingnotes.models.entities.Note
import io.reactivex.Single

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ApiNote) : Long

    @Update
    suspend fun updateNode(note:ApiNote)

    @Delete
    suspend fun deleteNode(note:ApiNote)

    @Query("select * from api_note where date between :startDay and :endDay order by date desc")
    fun getTodayNotes(startDay:Long,endDay:Long) : LiveData<MutableList<ApiNote>>

    @Query("select * from api_note where id=:id1")
    fun getNoteById(id1:Long) : ApiNote?
}