package com.amaizzzing.amaizingnotes

import android.app.Application
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.models.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.models.interactors.TodayNotesInteractorImpl
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepositoryImpl

class NotesApplication : Application() {
    val todayNoteDataSource:TodayNoteDatasource = TodayNoteDatasourceImpl()
    val todayNoteRepository:TodayNoteRepository = TodayNoteRepositoryImpl(todayNoteDataSource)
    val todayNoteInteractor:TodayNotesInteractor = TodayNotesInteractorImpl(todayNoteRepository)

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getMyTodayNoteInteractor() = todayNoteInteractor

    companion object {
        lateinit var instance : NotesApplication private set
    }
}