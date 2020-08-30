package com.amaizzzing.amaizingnotes.model.di.modules

import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class NoteRepositoryModule(val noteDataSource:TodayNoteDatasource) {
    @Provides
    fun getNoteRepositoryModule():TodayNoteRepository = TodayNoteRepositoryImpl(noteDataSource)
}