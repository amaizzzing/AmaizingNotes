package com.amaizzzing.amaizingnotes.viewmodel.viewmodel_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amaizzzing.amaizingnotes.viewmodel.AddNoteViewModel
import com.amaizzzing.amaizingnotes.viewmodel.CalendarViewModel
import com.amaizzzing.amaizingnotes.viewmodel.NotFinishViewModel
import com.amaizzzing.amaizingnotes.viewmodel.ResultsViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    abstract fun bindCalendarViewModel(calendarViewModel: CalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddNoteViewModel::class)
    abstract fun bindAddNoteViewModel(addNoteViewModel: AddNoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotFinishViewModel::class)
    abstract fun bindNotFinishViewModel(notFinishViewModel: NotFinishViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResultsViewModel::class)
    abstract fun bindResultsViewModel(notFinishViewModel: ResultsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

