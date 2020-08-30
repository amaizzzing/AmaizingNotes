package com.amaizzzing.amaizingnotes

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.model.db.AppDatabase
import com.amaizzzing.amaizingnotes.model.di.components.DaggerDiNotesComponent
import com.amaizzzing.amaizingnotes.model.di.components.DiNotesComponent
import com.amaizzzing.amaizingnotes.model.di.modules.*
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepositoryImpl
import com.amaizzzing.amaizingnotes.utils.DB_NAME
import com.amaizzzing.amaizingnotes.utils.MyNotificationChannel
import javax.inject.Inject

class NotesApplication : Application() {
    lateinit var noteComponent: DiNotesComponent

    @Inject
    lateinit var todayNoteDataSource: TodayNoteDatasource

    @Inject
    lateinit var todayNoteRepository: TodayNoteRepository

    @Inject
    lateinit var todayNoteInteractor: TodayNotesInteractor

    @Inject
    lateinit var notificationChannel: MyNotificationChannel

    @Inject
    lateinit var appDataBase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        instance = this

        noteComponent = DaggerDiNotesComponent
            .builder()
            .appDatabaseModule(AppDatabaseModule())
            .noteNotificationModule(NoteNotificationModule())
            .noteDatasourceModule(NoteDatasourceModule())
            .noteRepositoryModule(NoteRepositoryModule(TodayNoteDatasourceImpl()))
            .noteInteractorModule(
                NoteInteractorModule(
                    TodayNoteRepositoryImpl(
                        TodayNoteDatasourceImpl()
                    )
                )
            )
            .build()
        noteComponent.injectDiApplication(this)

        notificationChannel.createNotificationChannel(
            resources.getString(R.string.id_notifications_channel),
            resources.getString(R.string.name_notifications_channel),
            resources.getString(R.string.description_notifications_channel)
        )
    }

    fun getMyTodayNoteInteractor() = todayNoteInteractor

    fun getAppDataBase(context: Context = applicationContext) = appDataBase

    fun getNotificationManager() = notificationChannel.notificationManager

    companion object {
        lateinit var instance: NotesApplication private set
    }
}

