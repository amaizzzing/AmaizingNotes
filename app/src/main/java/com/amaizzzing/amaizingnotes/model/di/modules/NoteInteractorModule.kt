package com.amaizzzing.amaizingnotes.model.di.modules

import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractorImpl
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import dagger.Module
import dagger.Provides

@Module
class NoteInteractorModule(val noteRepository:TodayNoteRepository) {
    @Provides
    fun getNoteInteractorModule() : TodayNotesInteractor = TodayNotesInteractorImpl(noteRepository)
}