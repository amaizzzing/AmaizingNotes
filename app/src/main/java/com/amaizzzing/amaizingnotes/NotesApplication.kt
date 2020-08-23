package com.amaizzzing.amaizingnotes

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.models.db.AppDatabase
import com.amaizzzing.amaizingnotes.models.db.NoteDao
import com.amaizzzing.amaizingnotes.models.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.models.interactors.TodayNotesInteractorImpl
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepositoryImpl

class NotesApplication : Application() {
    val todayNoteDataSource:TodayNoteDatasource = TodayNoteDatasourceImpl()
    val todayNoteRepository:TodayNoteRepository = TodayNoteRepositoryImpl(todayNoteDataSource)
    val todayNoteInteractor:TodayNotesInteractor = TodayNotesInteractorImpl(todayNoteRepository)

    var appDataBase: AppDatabase? = null
    var noteDao : NoteDao? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getMyTodayNoteInteractor() = todayNoteInteractor

    fun getAppDataBase(context:Context = applicationContext): AppDatabase? {
        if (appDataBase == null){
            synchronized(AppDatabase::class){
                appDataBase = Room.databaseBuilder(context, AppDatabase::class.java, "AmaizingNotesDB").build()
                noteDao = appDataBase?.noteDao()
            }
        }
        return appDataBase
    }

    fun getAppNoteDao() = noteDao

    fun destroyDataBase(){
        appDataBase = null
    }

    companion object {
        lateinit var instance : NotesApplication private set

    }
}