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

    @Query("select count(id) from api_note where isDone=1 and date between :startDay and :endDay")
    fun getCountFinishTasks(startDay: Long, endDay: Long): Int

    @Query("select * from api_note where id=:id1")
    fun getNoteById(id1: Long): Maybe<ApiNote>

    @Query("select * from api_note where isDone=0 order by date desc")
    fun getNotFinishNotes(): LiveData<MutableList<ApiNote>>

    /*
    * запрос на подсчет разницы среднего количества выполненных задач за прошедшие дни и выполненных за сегодняшний день
    * для подсчета рейтинга в достижениях
     */
    @Query(
        "select avg(count) - (select count(id) from api_note where isDone=1 and strftime('%Y-%m-%d', date/1000, 'unixepoch') = strftime('%Y-%m-%d', 'now', 'localtime')) " +
                "from (" +
                "    SELECT strftime('%Y-%m-%d', date/1000, 'unixepoch') as normdate,count(id) as count" +
                "    FROM api_note" +
                "    where normdate != strftime('%Y-%m-%d', 'now', 'localtime') and isDone=1" +
                "    Group by normdate" +
                ")nested"
    )
    fun getCoefRatingForDays(): Double
}