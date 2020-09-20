package com.amaizzzing.amaizingnotes.model.di.components

import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.di.modules.AppDatabaseModule
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.viewmodel.viewmodel_factory.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ClearModule::class,
        //NoteNotificationModule::class,
        AppDatabaseModule::class,
        ViewModelModule::class
    ]
)
interface DiNotesComponent {
    //fun injectDiApplication(diApplication: NotesApplication)
}