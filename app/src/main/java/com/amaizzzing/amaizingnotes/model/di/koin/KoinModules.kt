package com.amaizzzing.amaizingnotes.model.di.koin

import android.app.NotificationManager
import android.content.Context
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.model.db.FirebaseDaoImpl
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractorImpl
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepositoryImpl
import com.amaizzzing.amaizingnotes.utils.MyNotificationChannel
import com.amaizzzing.amaizingnotes.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseDaoImpl(get(), get()) } bind TodayNoteDatasource::class
    single { TodayNotesInteractorImpl(get()) } bind TodayNotesInteractor::class
    single { TodayNoteRepositoryImpl(get()) } bind TodayNoteRepository::class
    single { TodayNoteDatasourceImpl(get()) }
    single { NotesApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager } bind NotificationManager::class
    single { MyNotificationChannel(get()) }
}
val calendarFragmentModule = module {
    viewModel { CalendarViewModel(get()) }
}
val addNoteFragmentModule = module {
    viewModel { AddNoteViewModel(get()) }
}
val notFinishViewModel = module {
    viewModel { NotFinishViewModel(get()) }
}
val resultsViewModel = module {
    viewModel { ResultsViewModel(get()) }
}
val splashViewModel = module {
    viewModel { SplashViewModel(get()) }
}
