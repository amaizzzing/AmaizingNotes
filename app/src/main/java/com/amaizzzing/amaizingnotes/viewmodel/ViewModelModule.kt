package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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
    abstract fun bindRepoViewModel(notFinishViewModel: NotFinishViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

