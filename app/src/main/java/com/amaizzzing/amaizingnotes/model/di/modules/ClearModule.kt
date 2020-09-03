package com.amaizzzing.amaizingnotes.model.di.modules

import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractorImpl
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClearModule {
    @Provides
    @Singleton
    fun getNoteInteractorModule(noteRepository:TodayNoteRepository) : TodayNotesInteractor = TodayNotesInteractorImpl(noteRepository)
    @Provides
    @Singleton
    fun getNoteDatasourceModule() : TodayNoteDatasource = TodayNoteDatasourceImpl()
    @Provides
    @Singleton
    fun getNoteRepositoryModule(noteDataSource:TodayNoteDatasource):TodayNoteRepository = TodayNoteRepositoryImpl(noteDataSource)
}