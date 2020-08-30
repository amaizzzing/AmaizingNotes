package com.amaizzzing.amaizingnotes.model.di.components

import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.di.modules.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NoteDatasourceModule::class,
    NoteInteractorModule::class,
    NoteRepositoryModule::class,
    NoteNotificationModule::class,
    AppDatabaseModule::class
])
interface DiNotesComponent {
    fun injectDiApplication(diApplication: NotesApplication)
}