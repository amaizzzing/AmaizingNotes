package com.amaizzzing.amaizingnotes.model.di.components

import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.view.fragments.AddNoteFragment
import com.amaizzzing.amaizingnotes.view.fragments.CalendarFragment
import com.amaizzzing.amaizingnotes.view.fragments.NotFinishFragment
import com.amaizzzing.amaizingnotes.view.fragments.ResultsFragment
import com.amaizzzing.amaizingnotes.viewmodel.viewmodel_factory.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class,
        ClearModule::class
    ]
)
interface Component2 {
    fun injectToCalendarFragment(fragment: CalendarFragment)
    fun injectToAddNoteFragment(fragment: AddNoteFragment)
    fun injectToNotFinishFragment(fragment: NotFinishFragment)
    fun injectToResultsFragment(fragment: ResultsFragment)
}