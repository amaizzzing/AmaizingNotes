package com.amaizzzing.amaizingnotes.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.entities.User
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.channels.ReceiveChannel

/*
@Dao
interface NoteDao : TodayNoteDatasource{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertNote(note: ApiNote): Maybe<Long>

    @Update
    override fun updateNote(note: ApiNote): Maybe<Int>

    @Delete
    override fun deleteNote(note: ApiNote): Maybe<Int>

    @Query("delete from api_note where id=:id1")
    override fun deleteNoteById(id1: Long): Maybe<Int>

    @Query("select * from api_note where typeNote=:typeRecord and date between :startDay and :endDay order by isDone,date desc")
    override fun getTodayNote(startDay: Long, endDay: Long, typeRecord:String): Flowable<List<ApiNote>>

    @Query("select * from api_note where date between :startDay and :endDay order by isDone,date desc")
    override fun getAllNotes(startDay: Long, endDay: Long): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>>

    @Query("select count(id) from api_note where isDone=1 and date between :startDay and :endDay")
    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int

    @Query("select * from api_note where id=:id1")
    override fun getNoteById(id1: Long): Maybe<ApiNote>

    @Query("select * from api_note where isDone=0 and typeNote='task' order by date desc")
    override fun getNotFinishNotes(): Flowable<List<ApiNote>>

    @Query("select * from api_note where nameNote like lower(:searchText) or text like lower(:searchText)")
    override fun searchNotes(searchText:String): Flowable<List<ApiNote>>

    */
/*
    * запрос на подсчет разницы среднего количества выполненных задач за прошедшие дни и выполненных за сегодняшний день
    * для подсчета рейтинга в достижениях
     *//*

    @Query(
        "select avg(count) - (select count(id) from api_note where isDone=1 and strftime('%Y-%m-%d', date/1000, 'unixepoch') = strftime('%Y-%m-%d', 'now', 'localtime')) " +
                "from (" +
                "    SELECT strftime('%Y-%m-%d', date/1000, 'unixepoch') as normdate,count(id) as count" +
                "    FROM api_note" +
                "    where normdate != strftime('%Y-%m-%d', 'now', 'localtime') and isDone=1" +
                "    Group by normdate" +
                ")nested"
    )
    override fun getCoefRatingForDays(): Double

    override fun getCurrentUser(): LiveData<User?> {
        TODO("Not yet implemented")
    }

    */
/*override fun subscribeToAllUserNotes(start: Long, end: Long): Flowable<List<ApiNote>> {
        TODO("Not yet implemented")
    }*//*

}*/
